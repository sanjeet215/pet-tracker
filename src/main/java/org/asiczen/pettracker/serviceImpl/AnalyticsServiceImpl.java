package org.asiczen.pettracker.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.Location;
import org.asiczen.pettracker.dto.response.PetHistoryResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.message.PetLastLocation;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.asiczen.pettracker.repository.PetLastLocationRepository;
import org.asiczen.pettracker.repository.TransFormedMessageRepository;
import org.asiczen.pettracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    TransFormedMessageRepository messageRepository;

    @Autowired
    PetLastLocationRepository petLastLocationRepository;

    @Override
    public List<TransFormedMessage> getLastLocationOfPet(String ownerId) {


        //Optional<List<TransFormedMessage>> lastLocationDtl = messageRepository.findByDevEuiOrderByTimeStampDesc(devEui);
        Optional<List<PetLastLocation>> lastLocationDtl = petLastLocationRepository.findByOwnerId(ownerId);
                //messageRepository.findByOwnerIdGroupByDevEuiOrderByTimeStampDesc(ownerId);

        if (lastLocationDtl.isPresent()) {

           // return lastLocationDtl.get();
            return lastLocationDtl.get().stream()
                    .map(this::getLastLocationOfPet)
                    .collect(Collectors.toList());

        } else {
            throw new ResourceNotFoundException("No data exists for this ");
        }
    }

    @Override
    public PetHistoryResponse findByDevEuiAndTimeStampBetween(String devEui, Date startTime, Date endTime) {

        log.info("Start time {} ", startTime.toString());
        log.info("End TIme {} ", endTime.toString());

        Optional<List<TransFormedMessage>> geoMessages = messageRepository.findByDevEuiAndTimeStampBetween(devEui, startTime, endTime);

        PetHistoryResponse response = new PetHistoryResponse();

        log.trace("Looping on data set Received.");

        if (geoMessages.isPresent()) {
            response.setDevEui(devEui);

            log.info("Some data are present so will process the data");

            List<Location> locationList = geoMessages.get()
                    .stream()
                    .map(geoMessage -> new Location(geoMessage.getLatitude(), geoMessage.getLongitude(), geoMessage.getTimeStamp()))
                    .collect(Collectors.toList());

            response.setLocationlist(locationList);

            response.setTimeStamp(new Date());


            return response;

        } else {
            throw new ResourceNotFoundException("No data exists for this pet {} " + devEui);
        }
    }

    private TransFormedMessage getLastLocationOfPet(PetLastLocation petLastLocation) {

        TransFormedMessage transFormedMessage = new TransFormedMessage();

        transFormedMessage.setOwnerId(petLastLocation.getOwnerId());
        transFormedMessage.setDevEui(petLastLocation.getDevEui());
        transFormedMessage.setLatitude(petLastLocation.getLatitude());
        transFormedMessage.setLongitude(petLastLocation.getLongitude());
        transFormedMessage.setTimeStamp(petLastLocation.getTimeStamp());

        transFormedMessage.setPetName(petLastLocation.getPetName());
        transFormedMessage.setPetBreed(petLastLocation.getPetBreed());
        transFormedMessage.setPetType(petLastLocation.getPetType());

        return transFormedMessage;
    }
}
