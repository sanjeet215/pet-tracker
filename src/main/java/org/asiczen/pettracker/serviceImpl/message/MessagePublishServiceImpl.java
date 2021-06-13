package org.asiczen.pettracker.serviceImpl.message;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.model.message.OriginalMessage;
import org.asiczen.pettracker.model.message.PetLastLocation;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.asiczen.pettracker.repository.*;
import org.asiczen.pettracker.service.message.AlertProcessService;
import org.asiczen.pettracker.service.message.MessagePublishService;
import org.asiczen.pettracker.source.IOTMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@EnableBinding(IOTMessageSource.class)
public class MessagePublishServiceImpl implements MessagePublishService {

    @Autowired
    IOTMessageSource iotMessageSource;

    @Autowired
    TransFormedMessageRepository messageRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CattleRepository cattleRepository;

    @Autowired
    OwnerConfigRepository ownerConfigRepository;

    @Autowired
    AlertProcessService alertProcessService;

    @Autowired
    PetLastLocationRepository lastLocationRepository;

    @Override
    public void publishMessage(OriginalMessage originalMessage) {
        TransFormedMessage transFormedMessage = new TransFormedMessage();

        Pet pet = petRepository.findByDeviceDeviceId(originalMessage.getEnd_device_ids().getDev_eui());

        Cattle cattle = cattleRepository.findByDeviceDeviceId(originalMessage.getEnd_device_ids().getDev_eui());

        if(pet != null) {

            transFormedMessage.setDevEui(originalMessage.getEnd_device_ids().getDev_eui());
            transFormedMessage.setLatitude(originalMessage.getLocation_solved().getLocation().getLatitude());
            transFormedMessage.setLongitude(originalMessage.getLocation_solved().getLocation().getLongitude());
            transFormedMessage.setTimeStamp(originalMessage.getReceived_at());

            Pet pet1 = new Pet();

            pet1.setId(pet.getId());
            pet1.setOwnerId(pet.getOwnerId());
            pet1.setName(pet.getName());
            pet1.setPetType(pet.getPetType());
            pet1.setPetBreed(pet.getPetBreed());
            pet1.setAnimalType(pet.getAnimalType());
            pet1.setPetColour(pet.getPetColour());
            pet1.setPetSex(pet.getPetSex());
            pet1.setKennelClubRegNo(pet.getKennelClubRegNo());
            pet1.setPetWeight(pet.getPetWeight());
            pet1.setPetDob(pet.getPetDob());

            pet1.setDevice(pet.getDevice());

            transFormedMessage.setAnimalType(pet.getAnimalType());
            transFormedMessage.setPet(pet1);

            transFormedMessage.setOwnerId(pet.getOwnerId());

            messageRepository.save(transFormedMessage);

            updateLastLocationOfPet(transFormedMessage);

            iotMessageSource.iotMessageSource().send(MessageBuilder.withPayload(transFormedMessage).build());
            Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(transFormedMessage.getOwnerId());

            if(ownerConfig.isPresent()) {
                alertProcessService.geoFenceViolation(transFormedMessage);
            }

        }else if (cattle != null) {

            transFormedMessage.setDevEui(originalMessage.getEnd_device_ids().getDev_eui());
            transFormedMessage.setLatitude(originalMessage.getLocation_solved().getLocation().getLatitude());
            transFormedMessage.setLongitude(originalMessage.getLocation_solved().getLocation().getLongitude());
            transFormedMessage.setTimeStamp(originalMessage.getReceived_at());

           // Cattle cattle1 = new Cattle();

            transFormedMessage.setAnimalType(cattle.getAnimalType());
            transFormedMessage.setCattle(cattle);

            transFormedMessage.setOwnerId(cattle.getOwnerId());

            messageRepository.save(transFormedMessage);

            updateLastLocationOfPet(transFormedMessage);

            iotMessageSource.iotMessageSource().send(MessageBuilder.withPayload(transFormedMessage).build());
            Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(transFormedMessage.getOwnerId());

            if(ownerConfig.isPresent()) {
                alertProcessService.geoFenceViolation(transFormedMessage);
            }

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
            petLastLocation.setPet(transFormedMessage.getPet());
            petLastLocation.setCattle(transFormedMessage.getCattle());


            lastLocationRepository.save(petLastLocation);

        }else {

            PetLastLocation petLastLocation = new PetLastLocation();

            petLastLocation.setOwnerId(transFormedMessage.getOwnerId());
            petLastLocation.setDevEui(transFormedMessage.getDevEui());

            petLastLocation.setTimeStamp(transFormedMessage.getTimeStamp());
            petLastLocation.setLatitude(transFormedMessage.getLatitude());
            petLastLocation.setLongitude(transFormedMessage.getLongitude());

            petLastLocation.setAnimalType(transFormedMessage.getAnimalType());
            petLastLocation.setPet(transFormedMessage.getPet());
            petLastLocation.setCattle(transFormedMessage.getCattle());

            lastLocationRepository.save(petLastLocation);
        }

    }

}
