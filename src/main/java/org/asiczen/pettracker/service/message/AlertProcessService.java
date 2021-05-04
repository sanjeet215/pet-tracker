package org.asiczen.pettracker.service.message;

import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.springframework.stereotype.Service;

@Service
public interface AlertProcessService {

    public void geoFenceViolation(TransFormedMessage transFormedMessage);

}
