package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.repository.CattleRepository;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;

@Component
public class PetServiceImpl implements PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CattleRepository cattleRepository;

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

    @Override
    public OwnerResponse deletePet(String ownerId, String petId) {

        Optional<Pet> pet = petRepository.findByOwnerIdAndId(ownerId, petId);
        if(pet.isPresent()) {
            petRepository.delete(pet.get());
            return new OwnerResponse("Pet deleted successfully.");
        }else {
            throw new ResourceNotFoundException("No pet available for delete.");
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pet editPet(Pet pet) {
        System.out.println("Get pet id:"+pet.getId());
        Optional<Pet> pet1 = petRepository.findByOwnerIdAndId(pet.getOwnerId(), pet.getId());

        if(pet1.isPresent()) {
            pet1.get().setName(pet.getName());
            pet1.get().setPetBreed(pet.getPetBreed());
            pet1.get().setPetWeight(pet.getPetWeight());
            pet1.get().setPetDob(pet.getPetDob());
            pet1.get().setPetColour(pet.getPetColour());
            pet1.get().setPetSex(pet.getPetSex());
            pet1.get().setKennelClubRegNo(pet.getKennelClubRegNo());
            pet1.get().setDevice(pet.getDevice());


            if(pet.getDevice() != null) {
                Optional<Pet> pet2 = petRepository.findByOwnerIdAndDeviceDeviceId(pet.getOwnerId(), pet.getDevice().getDeviceId());
                Optional<Cattle> cattle = cattleRepository.findByOwnerIdAndDeviceDeviceId(pet.getOwnerId(), pet.getDevice().getDeviceId());
                if(pet2.isPresent()){
                    pet2.get().setDevice(null);
                    petRepository.save(pet2.get());

                }else if(cattle.isPresent()){
                    cattle.get().setDevice(null);
                    cattleRepository.save(cattle.get());
                }
            }

            petRepository.save(pet1.get());
            return pet1.get();

        }else {
            throw new ResourceNotFoundException("No pet available for update.");
        }
    }
}
