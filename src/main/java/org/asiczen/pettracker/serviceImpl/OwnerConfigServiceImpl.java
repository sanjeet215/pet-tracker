package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.dto.GeofenceConfigRequest;
import org.asiczen.pettracker.exception.InternalServerError;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.repository.OwnerConfigRepository;
import org.asiczen.pettracker.service.OwnerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OwnerConfigServiceImpl implements OwnerConfigService {

    @Autowired
    OwnerConfigRepository ownerConfigRepository;

    @Override
    public String setOwnerConfig(GeofenceConfigRequest geofenceConfigRequest) {

        Optional<OwnerConfig> ownerConfig1 = ownerConfigRepository.findByOwnerId(geofenceConfigRequest.getOwnerId());
        if(ownerConfig1.isPresent()) {

            OwnerConfig ownerConfig = ownerConfig1.get();

            //ownerConfig.setOwnerId(geofenceConfigRequest.getOwnerId());
            ownerConfig.setRadiusInMtr(geofenceConfigRequest.getRadiusInMtr());
            ownerConfig.setLat(geofenceConfigRequest.getLat());
            ownerConfig.setLng(geofenceConfigRequest.getLng());
            ownerConfig.setContactNumber(geofenceConfigRequest.getContactNumber());

            try {
                ownerConfigRepository.save(ownerConfig);
                return "Owner configuration set successfully.";

            }catch (Exception exception) {
                throw new InternalServerError("Error while set config.");
            }

        }else {

            OwnerConfig ownerConfig = new OwnerConfig();

            ownerConfig.setOwnerId(geofenceConfigRequest.getOwnerId());
            ownerConfig.setRadiusInMtr(geofenceConfigRequest.getRadiusInMtr());
            ownerConfig.setLat(geofenceConfigRequest.getLat());
            ownerConfig.setLng(geofenceConfigRequest.getLng());
            ownerConfig.setContactNumber(geofenceConfigRequest.getContactNumber());

            try {
                ownerConfigRepository.save(ownerConfig);
                return "Owner configuration set successfully.";

            }catch (Exception exception) {
                throw new InternalServerError("Error while set config.");
            }
        }


    }

    @Override
    public OwnerConfig getOwnerConfig(String ownerId) {

        Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(ownerId);

        if (ownerConfig.isPresent()) {

            return ownerConfig.get();

        }else {
            throw new ResourceNotFoundException("Invalid owner id to get owner configuration.");
        }
    }

    @Override
    public OwnerConfig disableGeofenceAlert(String ownerId, boolean status) {

        Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(ownerId);

        if (ownerConfig.isPresent()) {

            OwnerConfig ownerConfig1 = ownerConfig.get();
            ownerConfig1.setGeoFenceAlertOn(status);
            ownerConfigRepository.save(ownerConfig1);
            return ownerConfig1;

        }else {
            throw new ResourceNotFoundException("Invalid owner id to get owner configuration.");
        }
    }

    @Override
    public OwnerConfig disableAllAlert(String ownerId, boolean status) {

        Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(ownerId);

        if (ownerConfig.isPresent()) {

            OwnerConfig ownerConfig1 = ownerConfig.get();
            ownerConfig1.setAlertOn(status);
            ownerConfigRepository.save(ownerConfig1);
            return ownerConfig1;

        }else {
            throw new ResourceNotFoundException("Invalid owner id to get owner configuration.");
        }
    }

    @Override
    public OwnerConfig updateOwnerConfig(GeofenceConfigRequest request) {

        Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(request.getOwnerId());

        if (ownerConfig.isPresent()) {

            OwnerConfig ownerConfig1 = ownerConfig.get();
            ownerConfig1.setRadiusInMtr(request.getRadiusInMtr());
            ownerConfig1.setLat(request.getLat());
            ownerConfig1.setLng(request.getLng());
            ownerConfig1.setContactNumber(request.getContactNumber());

            ownerConfigRepository.save(ownerConfig1);

            return ownerConfig1;

        }else {
            throw new ResourceNotFoundException("Invalid owner id to get owner configuration.");
        }
    }
}
