package org.asiczen.pettracker.controller;


import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.PetHistoryRequest;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/analytics")
@Slf4j
public class AnalyticsController {

    @Autowired
    AnalyticsService analyticsService;

    @GetMapping("/lastpositiondtl")
    public ResponseEntity<?> getLastLocation(@Valid @RequestParam String ownerId) {

        log.trace("Getting last location of pet.");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Pet Last Location extracted successfully", analyticsService.getLastLocationOfPet(ownerId)));

    }

    @PostMapping("/history")
    public ResponseEntity<?> getVehicleHistory(@Valid @RequestBody PetHistoryRequest request) {

        log.trace("extracting pet history");

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Pet history extracted successfully",
                        analyticsService.findByDevEuiAndTimeStampBetween(request.getDevEui(),	request.getStartDateTime(), request.getEndDateTime())));
    }
}
