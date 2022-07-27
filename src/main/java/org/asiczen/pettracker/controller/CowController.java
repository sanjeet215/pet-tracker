package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Cow;
import org.asiczen.pettracker.service.CowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owner")
@Slf4j
public class CowController {

    @Autowired
    CowService cowService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllCow")
    public List<Cow> getCowList(@Valid @RequestParam String ownerId) {

        return cowService.getAllCowList(ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/editCow")
    public ResponseEntity<?> editPet(@Valid @RequestBody Cow cow) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Cow updated successfully", cowService.editCow(cow)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteCow")
    public OwnerResponse deletePet(@Valid @RequestParam String ownerId, @RequestParam String petId) {
        return cowService.deleteCow(ownerId, petId);
    }
}
