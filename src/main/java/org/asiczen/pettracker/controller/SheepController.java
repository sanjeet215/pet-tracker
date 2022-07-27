package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Sheep;
import org.asiczen.pettracker.service.SheepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owner")
@Slf4j
public class SheepController {

    @Autowired
    SheepService sheepService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllSheep")
    public List<Sheep> getDeviceList(@Valid @RequestParam String ownerId) {

        return sheepService.getAllSheepList(ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/editSheep")
    public ResponseEntity<?> editCattle(@Valid @RequestBody Sheep sheep) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Sheep updated successfully", sheepService.editSheep(sheep)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteSheep")
    public OwnerResponse deleteCattle(@Valid @RequestParam String ownerId, @RequestParam String cattleId) {
        return sheepService.deleteSheep(ownerId, cattleId);
    }
}
