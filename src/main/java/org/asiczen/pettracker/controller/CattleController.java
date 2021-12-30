package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.service.CattleService;
import org.asiczen.pettracker.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owner")
@Slf4j
public class CattleController {

    @Autowired
    CattleService cattleService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllCattle")
    public List<Cattle> getDeviceList(@Valid @RequestParam String ownerId) {

        return cattleService.getAllCattleList(ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/editCattle")
    public ResponseEntity<?> editCattle(@Valid @RequestBody Cattle cattle) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Cattle updated successfully", cattleService.editCattle(cattle)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteCattle")
    public OwnerResponse deleteCattle(@Valid @RequestParam String ownerId, @RequestParam String cattleId) {
        return cattleService.deleteCattle(ownerId, cattleId);
    }
}
