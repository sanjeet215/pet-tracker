package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.repository.CattleRepository;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.service.CattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;

@Component
public class CattleServiceImpl implements CattleService {

    @Autowired
    CattleRepository cattleRepository;

    @Autowired
    PetRepository petRepository;

    @Override
    public List<Cattle> getAllCattleList(String ownerId) {
        Optional<List<Cattle>> cattle = cattleRepository.findByOwnerId(ownerId);

        if (cattle.isPresent()) {
            return cattle.get();
        } else {
            throw new ResourceNotFoundException("No pet registered in this owner yet ....");
        }
    }

    @Override
    public long getCattleCount(String ownerId) {

        Optional<List<Cattle>> cattle = cattleRepository.findByOwnerId(ownerId);

        return (cattle.isPresent()) ? cattle.get().size() : 0L;
    }

    @Override
    public OwnerResponse deleteCattle(String ownerId, String cattleId) {

        Optional<Cattle> cattle = cattleRepository.findByOwnerIdAndId(ownerId, cattleId);
        if(cattle.isPresent()) {
            cattleRepository.delete(cattle.get());
            return new OwnerResponse("Cattle deleted successfully.");
        }else {
            throw new ResourceNotFoundException("No cattle available for delete.");
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public Cattle editCattle(Cattle cattle) {
        Optional<Cattle> cattle1 = cattleRepository.findByOwnerIdAndId(cattle.getOwnerId(), cattle.getId());

        if(cattle1.isPresent()) {
            cattle1.get().setEarTagNumber(cattle.getEarTagNumber());
            cattle1.get().setBreed(cattle.getBreed());
            cattle1.get().setDob(cattle.getDob());
            cattle1.get().setSex(cattle.getSex());
            cattle1.get().setDateOfPassportIssue(cattle.getDateOfPassportIssue());
            cattle1.get().setDevice(cattle.getDevice());


            if(cattle.getDevice() != null) {
                Optional<Cattle> cattle2 = cattleRepository.findByOwnerIdAndDeviceDeviceId(cattle.getOwnerId(), cattle.getDevice().getDeviceId());
                Optional<Pet> pet = petRepository.findByOwnerIdAndDeviceDeviceId(cattle.getOwnerId(), cattle.getDevice().getDeviceId());
                if(cattle2.isPresent()){
                    cattle2.get().setDevice(null);
                    cattleRepository.save(cattle2.get());

                }else if(pet.isPresent()){
                    pet.get().setDevice(null);
                    petRepository.save(pet.get());
                }
            }

            cattleRepository.save(cattle1.get());
            return cattle1.get();

        }else {
            throw new ResourceNotFoundException("No cattle available for update.");
        }
    }
}
