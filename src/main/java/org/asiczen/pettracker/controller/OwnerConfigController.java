package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.GeofenceConfigRequest;
import org.asiczen.pettracker.dto.PetHistoryRequest;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.model.message.GeofenceLocation;
import org.asiczen.pettracker.service.OwnerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/config")
@Slf4j
public class OwnerConfigController {

    @Autowired
    OwnerConfigService ownerConfigService;

    @PostMapping("/geofence")
    @ResponseStatus(HttpStatus.OK)
    public String setOwnerConfig(@Valid @RequestBody GeofenceConfigRequest request) {

        log.trace("Set owner config");
        return ownerConfigService.setOwnerConfig(request);


    }

    @GetMapping("/geofence")
    @ResponseStatus(HttpStatus.OK)
    public OwnerConfig getOwnerConfig(@Valid @RequestParam String ownerId) {

        log.trace("Set owner config");
        return ownerConfigService.getOwnerConfig(ownerId);


    }

    @PatchMapping("/geofence/disable")
    public ResponseEntity<ApiResponse> updateGeoAlertStatus(@Valid @RequestParam boolean status, @Valid @RequestParam String ownerId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Status of geofence alert is updated.", ownerConfigService.disableGeofenceAlert(ownerId, status)));
    }

    @PatchMapping("/alert/disable")
    public ResponseEntity<ApiResponse> updateAlertStatus(@Valid @RequestParam boolean status, @Valid @RequestParam String ownerId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Status of alert is updated.", ownerConfigService.disableAllAlert(ownerId, status)));
    }


    @PatchMapping("/geofence")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateOwnerConfig(@Valid @RequestBody GeofenceConfigRequest updateRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Owner config updated.", ownerConfigService.updateOwnerConfig(updateRequest)));

    }


    @PatchMapping("/geofence/locations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> setOrUpdateGeofenceLocations(@Valid @RequestBody GeofenceLocation geofenceLocation) {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Geofence location updated.", ownerConfigService.setOrUpdateGeofencePoints(geofenceLocation)));

    }


    @GetMapping("/geofence/getLocations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getGeofenceLocations(@Valid @RequestParam String ownerId) {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Get all geofence locations.", ownerConfigService.getGeofenceLocations(ownerId)));

    }

}
