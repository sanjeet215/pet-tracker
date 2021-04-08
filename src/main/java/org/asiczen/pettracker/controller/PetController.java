package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
@Slf4j
public class PetController {

    @Autowired
    PetService petService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pet")
    public List<Pet> getDeviceList(@Valid @RequestParam String ownerId) {
        return petService.getAllPetList(ownerId);
    }
}
