package org.asiczen.pettracker.service.message;

import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.springframework.stereotype.Service;

@Service
public interface AlertProcessService {

    public void geoFenceViolation(TransFormedMessage transFormedMessage);

    public void temperatureAlert(TransFormedMessage transFormedMessage);

    public void lowBatteryAlert(TransFormedMessage transFormedMessage);

    public void cattleNoMovementAlert(TransFormedMessage transFormedMessage);

}
