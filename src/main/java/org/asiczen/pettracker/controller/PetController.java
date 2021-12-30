package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.CountResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.model.Pet;
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
public class PetController {

    @Autowired
    PetService petService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllPet")
    public List<Pet> getDeviceList(@Valid @RequestParam String ownerId) {

        return petService.getAllPetList(ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/editPet")
    public ResponseEntity<?> editPet(@Valid @RequestBody Pet pet) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Pet updated successfully", petService.editPet(pet)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deletePet")
    public OwnerResponse deletePet(@Valid @RequestParam String ownerId, @RequestParam String petId) {
        return petService.deletePet(ownerId, petId);
    }
}
