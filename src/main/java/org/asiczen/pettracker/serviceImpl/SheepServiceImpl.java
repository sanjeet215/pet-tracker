package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cow;
import org.asiczen.pettracker.model.Sheep;
import org.asiczen.pettracker.repository.CattleRepository;
import org.asiczen.pettracker.repository.CowRepository;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.repository.SheepRepository;
import org.asiczen.pettracker.service.SheepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;

@Component
public class SheepServiceImpl implements SheepService {

    @Autowired
    SheepRepository sheepRepository;

    @Autowired
    CowRepository cowRepository;

    @Override
    public List<Sheep> getAllSheepList(String ownerId) {
        Optional<List<Sheep>> sheep = sheepRepository.findByOwnerId(ownerId);

        if (sheep.isPresent()) {
            return sheep.get();
        } else {
            throw new ResourceNotFoundException("No pet registered in this owner yet ....");
        }
    }

    @Override
    public long getSheepCount(String ownerId) {

        Optional<List<Sheep>> cattle = sheepRepository.findByOwnerId(ownerId);

        return (cattle.isPresent()) ? cattle.get().size() : 0L;
    }

    @Override
    public OwnerResponse deleteSheep(String ownerId, String cattleId) {

        Optional<Sheep> cattle = sheepRepository.findByOwnerIdAndId(ownerId, cattleId);
        if(cattle.isPresent()) {
            sheepRepository.delete(cattle.get());
            return new OwnerResponse("Sheep deleted successfully.");
        }else {
            throw new ResourceNotFoundException("No sheep available for delete.");
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public Sheep editSheep(Sheep sheep) {
        Optional<Sheep> sheep1 = sheepRepository.findByOwnerIdAndId(sheep.getOwnerId(), sheep.getId());

        if(sheep1.isPresent()) {
            //sheep1.get().setEarTagNumber(sheep.getEarTagNumber());
            sheep1.get().setDateOfBirth(sheep.getDateOfBirth());
            sheep1.get().setDateOfDeath(sheep.getDateOfDeath());
            sheep1.get().setDateOfMovement(sheep.getDateOfMovement());
            sheep1.get().setMovementFrom(sheep.getMovementFrom());
            sheep1.get().setBodyTemperature(sheep.getBodyTemperature());
            sheep1.get().setMovementTo(sheep.getMovementTo());

            sheep1.get().setDevice(sheep.getDevice());


            if(sheep.getDevice() != null) {
                Optional<Sheep> sheep2 = sheepRepository.findByOwnerIdAndDeviceDeviceId(sheep.getOwnerId(), sheep.getDevice().getDeviceId());
                Optional<Cow> cow = cowRepository.findByOwnerIdAndDeviceDeviceId(sheep.getOwnerId(), sheep.getDevice().getDeviceId());
                if(sheep2.isPresent()){
                    sheep2.get().setDevice(null);
                    sheepRepository.save(sheep2.get());

                }else if(cow.isPresent()){
                    cow.get().setDevice(null);
                    cowRepository.save(cow.get());
                }
            }

            sheepRepository.save(sheep1.get());
            return sheep1.get();

        }else {
            throw new ResourceNotFoundException("No sheep available for update.");
        }
    }
}
