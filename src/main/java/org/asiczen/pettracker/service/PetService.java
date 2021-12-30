package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Pet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PetService {

    List<Pet> getAllPetList(String ownerId);

    long getPetCount(String ownerId);

    OwnerResponse deletePet(String ownerId, String petId);

    Pet editPet(Pet pet);
}
