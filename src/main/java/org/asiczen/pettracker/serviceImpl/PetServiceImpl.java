package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PetServiceImpl implements PetService {

    @Autowired
    PetRepository petRepository;

    @Override
    public List<Pet> getAllPetList(String ownerId) {
        Optional<List<Pet>> pets = petRepository.findByOwnerId(ownerId);

        if (pets.isPresent()) {
            return pets.get();
        } else {
            throw new ResourceNotFoundException("No pet registered in this owner yet ....");
        }
    }

    @Override
    public long getPetCount(String ownerId) {
        Optional<List<Pet>> pets = petRepository.findByOwnerId(ownerId);

        return (pets.isPresent()) ? pets.get().size() : 0L;

    }
}
