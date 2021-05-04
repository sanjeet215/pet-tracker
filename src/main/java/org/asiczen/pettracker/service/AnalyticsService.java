package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.response.PetHistoryResponse;
import org.asiczen.pettracker.model.message.PetLastLocation;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface AnalyticsService {

    public List<TransFormedMessage> getLastLocationOfPet(String ownerId);

    public PetHistoryResponse findByDevEuiAndTimeStampBetween(String vehicleNumber, Date startTime, Date endTime);

}
