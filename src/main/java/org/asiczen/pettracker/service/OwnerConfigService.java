package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.GeofenceConfigRequest;
import org.asiczen.pettracker.model.OwnerConfig;
import org.springframework.stereotype.Service;

@Service
public interface OwnerConfigService {

    public String setOwnerConfig(GeofenceConfigRequest geofenceConfigRequest);

    public OwnerConfig getOwnerConfig(String ownerId);

    public OwnerConfig disableGeofenceAlert(String ownerId, boolean status);

    public OwnerConfig disableAllAlert(String ownerId, boolean status);

    public OwnerConfig updateOwnerConfig(GeofenceConfigRequest request);

}
