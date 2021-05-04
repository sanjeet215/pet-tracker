package org.asiczen.pettracker.serviceImpl.message;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.model.message.OriginalMessage;
import org.asiczen.pettracker.model.message.PetLastLocation;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.asiczen.pettracker.repository.OwnerConfigRepository;
import org.asiczen.pettracker.repository.PetLastLocationRepository;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.repository.TransFormedMessageRepository;
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
    OwnerConfigRepository ownerConfigRepository;

    @Autowired
    AlertProcessService alertProcessService;

    @Autowired
    PetLastLocationRepository lastLocationRepository;

    @Override
    public void publishMessage(OriginalMessage originalMessage) {
        TransFormedMessage transFormedMessage = new TransFormedMessage();

        Pet pet = petRepository.findByDeviceDeviceId(originalMessage.getEnd_device_ids().getDev_eui());

        if(pet != null) {

            transFormedMessage.setDevEui(originalMessage.getEnd_device_ids().getDev_eui());
            transFormedMessage.setLatitude(originalMessage.getLocation_solved().getLocation().getLatitude());
            transFormedMessage.setLongitude(originalMessage.getLocation_solved().getLocation().getLongitude());
            transFormedMessage.setTimeStamp(originalMessage.getReceived_at());

            transFormedMessage.setPetName(pet.getName());
            transFormedMessage.setPetType(pet.getPetType());
            transFormedMessage.setPetBreed(pet.getPetBreed());

            transFormedMessage.setOwnerId(pet.getOwnerId());

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

            petLastLocation.setPetName(transFormedMessage.getPetName());
            petLastLocation.setPetBreed(transFormedMessage.getPetBreed());
            petLastLocation.setPetType(transFormedMessage.getPetType());

            lastLocationRepository.save(petLastLocation);

        }else {

            PetLastLocation petLastLocation = new PetLastLocation();

            petLastLocation.setOwnerId(transFormedMessage.getOwnerId());
            petLastLocation.setDevEui(transFormedMessage.getDevEui());

            petLastLocation.setTimeStamp(transFormedMessage.getTimeStamp());
            petLastLocation.setLatitude(transFormedMessage.getLatitude());
            petLastLocation.setLongitude(transFormedMessage.getLongitude());

            petLastLocation.setPetName(transFormedMessage.getPetName());
            petLastLocation.setPetBreed(transFormedMessage.getPetBreed());
            petLastLocation.setPetType(transFormedMessage.getPetType());

            lastLocationRepository.save(petLastLocation);
        }

    }

}
