package org.asiczen.pettracker.serviceImpl.message;

import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cow;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.model.Sheep;
import org.asiczen.pettracker.model.message.OriginalMessage;
import org.asiczen.pettracker.model.message.PetLastLocation;
import org.asiczen.pettracker.model.message.PetTemperatureCalculate;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.asiczen.pettracker.repository.*;
import org.asiczen.pettracker.service.message.AlertProcessService;
import org.asiczen.pettracker.service.message.MessagePublishService;
import org.asiczen.pettracker.source.IOTMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.swing.text.Document;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@Slf4j
@EnableBinding(IOTMessageSource.class)
public class MessagePublishServiceImpl implements MessagePublishService {

    @Autowired
    IOTMessageSource iotMessageSource;

    @Autowired
    TransFormedMessageRepository messageRepository;

    @Autowired
    //PetRepository petRepository;
    CowRepository cowRepository;

    @Autowired
    //CattleRepository cattleRepository;
    SheepRepository sheepRepository;

    @Autowired
    OwnerConfigRepository ownerConfigRepository;

    @Autowired
    AlertProcessService alertProcessService;

    @Autowired
    PetLastLocationRepository lastLocationRepository;

    @Autowired
    PetTemperatureRepository petTemperatureRepository;

    private String temperatureStatus = "normal";

    @Override
    public void publishMessage(OriginalMessage originalMessage) {

        System.out.println(originalMessage);

        System.out.println(originalMessage.getTemperature());

        TransFormedMessage transFormedMessage = new TransFormedMessage();

        Cow cow = cowRepository.findByDeviceDeviceId(originalMessage.getEnd_device_ids().getDev_eui());

        Sheep sheep = sheepRepository.findByDeviceDeviceId(originalMessage.getEnd_device_ids().getDev_eui());

        if(cow != null) {

            Optional<PetLastLocation> petLastLocationOld = lastLocationRepository.findByDevEui(originalMessage.getEnd_device_ids().getDev_eui());

            if (originalMessage.getTemperature() != null) {
                temperatureCalculation(originalMessage, cow.getOwnerId());
            }

            transFormedMessage.setDevEui(originalMessage.getEnd_device_ids().getDev_eui());
            if (originalMessage.getLocation_solved().getLocation().getLatitude() != 0.0) {
                transFormedMessage.setLatitude(originalMessage.getLocation_solved().getLocation().getLatitude());
                transFormedMessage.setLongitude(originalMessage.getLocation_solved().getLocation().getLongitude());
            }else {
                if(petLastLocationOld.isPresent()) {
                    transFormedMessage.setLatitude(petLastLocationOld.get().getLatitude());
                    transFormedMessage.setLongitude(petLastLocationOld.get().getLongitude());
                }
            }
            transFormedMessage.setTimeStamp(originalMessage.getReceived_at());

            Cow cow1 = new Cow();

            cow1.setId(cow.getId());
            cow1.setOwnerId(cow.getOwnerId());
            cow1.setBreed(cow.getBreed());
            cow1.setEarTagNumber(cow.getEarTagNumber());
            cow1.setPassportNo(cow.getPassportNo());
            cow1.setCphNumber(cow.getCphNumber());
            cow1.setBodyTemperature(cow.getBodyTemperature());
            cow1.setDateOfDeath(cow.getDateOfDeath());
            cow1.setDateOfMovement(cow.getDateOfMovement());
            cow1.setMovementFrom(cow.getMovementFrom());
            cow1.setMovementTo(cow.getMovementTo());
            cow1.setAnimalType(cow.getAnimalType());
            cow1.setSex(cow.getSex());
            cow1.setDateOfBirth(cow.getDateOfBirth());

            cow1.setDevice(cow.getDevice());

            transFormedMessage.setAnimalType(cow.getAnimalType());
            transFormedMessage.setCow(cow1);

            transFormedMessage.setOwnerId(cow.getOwnerId());

            if (originalMessage.getVoltage() != null) {

                transFormedMessage.setBattery(batteryPercentageCalculation(originalMessage.getVoltage()));
                transFormedMessage.setTemperature(temperatureStatus);
            }else {
                if(petLastLocationOld.isPresent()) {
                    System.out.println("Inside else");
                    System.out.println(petLastLocationOld.get().getBattery());
                    transFormedMessage.setBattery(petLastLocationOld.get().getBattery());
                    transFormedMessage.setTemperature(petLastLocationOld.get().getTemperature());
                }
            }

            messageRepository.save(transFormedMessage);

            updateLastLocationOfPet(transFormedMessage);

            iotMessageSource.iotMessageSource().send(MessageBuilder.withPayload(transFormedMessage).build());
            Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(transFormedMessage.getOwnerId());

            if(ownerConfig.isPresent()) {
                alertProcessService.geoFenceViolation(transFormedMessage);
            }

        }else if (sheep != null) {

            Optional<PetLastLocation> petLastLocationOld = lastLocationRepository.findByDevEui(originalMessage.getEnd_device_ids().getDev_eui());

            if (originalMessage.getTemperature() != null) {
                temperatureCalculation(originalMessage, sheep.getOwnerId());
            }

            transFormedMessage.setDevEui(originalMessage.getEnd_device_ids().getDev_eui());
            if (originalMessage.getLocation_solved().getLocation().getLatitude() != 0.0) {
                transFormedMessage.setLatitude(originalMessage.getLocation_solved().getLocation().getLatitude());
                transFormedMessage.setLongitude(originalMessage.getLocation_solved().getLocation().getLongitude());
            }else {
                if(petLastLocationOld.isPresent()) {
                    transFormedMessage.setLatitude(petLastLocationOld.get().getLatitude());
                    transFormedMessage.setLongitude(petLastLocationOld.get().getLongitude());
                }
            }
            transFormedMessage.setTimeStamp(originalMessage.getReceived_at());

           // Cattle cattle1 = new Cattle();

            transFormedMessage.setAnimalType(sheep.getAnimalType());
            transFormedMessage.setSheep(sheep);

            transFormedMessage.setOwnerId(sheep.getOwnerId());

            if (originalMessage.getVoltage() != null) {
                transFormedMessage.setBattery(batteryPercentageCalculation(originalMessage.getVoltage()));
                transFormedMessage.setTemperature(temperatureStatus);
            }else {
                transFormedMessage.setBattery(petLastLocationOld.get().getBattery());
                transFormedMessage.setTemperature(petLastLocationOld.get().getTemperature());
            }

            messageRepository.save(transFormedMessage);

            updateLastLocationOfPet(transFormedMessage);

            iotMessageSource.iotMessageSource().send(MessageBuilder.withPayload(transFormedMessage).build());
            Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(transFormedMessage.getOwnerId());

            if(ownerConfig.isPresent()) {
                alertProcessService.geoFenceViolation(transFormedMessage);
            }

        }
    }

    @Override
    public String resetTemperature(String deviceId) {

        List<PetTemperatureCalculate> petTemperatureCalculate = petTemperatureRepository.findByDevEui(deviceId);
        if (petTemperatureCalculate.isEmpty()) {

            throw new ResourceNotFoundException("Device id not found.");

        }else {

            System.out.println(petTemperatureCalculate);

            MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "test");
            //Query q = new Query(where("_id").in(petTemperatureCalculate));
            Query q = Query.query(Criteria.where("devEui").is(petTemperatureCalculate.get(0).getDevEui()));
            List<Document> deletedDocs = mongoOps.findAllAndRemove(q, "petalerttemperature");
// -or-
//List<Document> deletedDocs = mongoOps.findAllAndRemove(q, "testColl");
            System.out.println(deletedDocs);

            return "Temperature reset successfully";
        }
    }

    public void updateLastLocationOfPet(TransFormedMessage transFormedMessage) {

        Optional<PetLastLocation> petLastLocationOld = lastLocationRepository.findByDevEui(transFormedMessage.getDevEui());



        if(petLastLocationOld.isPresent()) {

            PetLastLocation petLastLocation = petLastLocationOld.get();

            petLastLocation.setTimeStamp(transFormedMessage.getTimeStamp());
            petLastLocation.setLatitude(transFormedMessage.getLatitude());
            petLastLocation.setLongitude(transFormedMessage.getLongitude());

            petLastLocation.setAnimalType(transFormedMessage.getAnimalType());
            petLastLocation.setCow(transFormedMessage.getCow());
            petLastLocation.setSheep(transFormedMessage.getSheep());

            petLastLocation.setTemperature(transFormedMessage.getTemperature());
            petLastLocation.setBattery(transFormedMessage.getBattery());


            lastLocationRepository.save(petLastLocation);

        }else {

            PetLastLocation petLastLocation = new PetLastLocation();

            petLastLocation.setOwnerId(transFormedMessage.getOwnerId());
            petLastLocation.setDevEui(transFormedMessage.getDevEui());

            petLastLocation.setTimeStamp(transFormedMessage.getTimeStamp());
            petLastLocation.setLatitude(transFormedMessage.getLatitude());
            petLastLocation.setLongitude(transFormedMessage.getLongitude());

            petLastLocation.setAnimalType(transFormedMessage.getAnimalType());
            petLastLocation.setCow(transFormedMessage.getCow());
            petLastLocation.setSheep(transFormedMessage.getSheep());

            petLastLocation.setTemperature(transFormedMessage.getTemperature());
            petLastLocation.setBattery(transFormedMessage.getBattery());

            lastLocationRepository.save(petLastLocation);
        }

    }

    private void temperatureCalculation(OriginalMessage originalMessage, String ownerId) {
        List<PetTemperatureCalculate> petTemperatureCalculateList= petTemperatureRepository.findByOwnerIdAndDevEui(ownerId, originalMessage.getEnd_device_ids().getDev_eui());

        if (petTemperatureCalculateList.stream().count() >= 30) {

            Double averageTemperature = petTemperatureCalculateList.stream()
                    .mapToDouble(PetTemperatureCalculate::getTemperature)
                    .average()
                    .orElse(0);

            long afterRound = Math.round(averageTemperature);

            if(afterRound == Long.parseLong(originalMessage.getTemperature())){

                temperatureStatus = "normal";

            }else if((afterRound + 1) == Long.parseLong(originalMessage.getTemperature())) {

                temperatureStatus = "normal";

            }else if((afterRound - 1) == Long.parseLong(originalMessage.getTemperature())) {

                temperatureStatus = "normal";

            }else if((afterRound + 2) <= Long.parseLong(originalMessage.getTemperature())) {

                temperatureStatus = "high";

            }else if((afterRound - 2) >= Long.parseLong(originalMessage.getTemperature())) {

                temperatureStatus = "low";

            }

            System.out.println("Average temperature :" + averageTemperature);

            //temperatureStatus = "high";

        }else {
            PetTemperatureCalculate petTemperatureCalculate = new PetTemperatureCalculate();

            petTemperatureCalculate.setOwnerId(ownerId);
            petTemperatureCalculate.setDevEui(originalMessage.getEnd_device_ids().getDev_eui());
            petTemperatureCalculate.setTemperature(Double.parseDouble(originalMessage.getTemperature()));
            petTemperatureCalculate.setBattery(batteryPercentageCalculation(originalMessage.getVoltage()));
            petTemperatureCalculate.setTimeStamp(originalMessage.getReceived_at());

            petTemperatureRepository.save(petTemperatureCalculate);


            temperatureStatus = "normal";



        }
    }

    private double batteryPercentageCalculation(String voltage) {

        double d = Double.parseDouble(voltage);

        if (d != 0.0) {
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);

            double number = (Double.parseDouble(df.format(d)) * 1000);

            double percentage = (100 - (((3200 - number) / 500) * 100));
            if (percentage >= 0.0) {

                return percentage;

            }else if (percentage >= 100.0){

                return 100.0;

            }else {

                return 1.0;

            }
        }else {
            return 0.0;
        }
    }

}
