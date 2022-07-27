package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cow;
import org.asiczen.pettracker.model.Sheep;
import org.asiczen.pettracker.repository.CowRepository;
import org.asiczen.pettracker.repository.SheepRepository;
import org.asiczen.pettracker.service.CowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;

@Component
public class CowServiceImpl implements CowService {

    @Autowired
    CowRepository cowRepository;

    @Autowired
    SheepRepository sheepRepository;

    @Override
    public List<Cow> getAllCowList(String ownerId) {
        Optional<List<Cow>> pets = cowRepository.findByOwnerId(ownerId);

        if (pets.isPresent()) {
            return pets.get();
        } else {
            throw new ResourceNotFoundException("No cow registered in this owner yet ....");
        }
    }

    @Override
    public long getCowCount(String ownerId) {
        Optional<List<Cow>> cows = cowRepository.findByOwnerId(ownerId);

        return (cows.isPresent()) ? cows.get().size() : 0L;

    }

    @Override
    public OwnerResponse deleteCow(String ownerId, String petId) {

        Optional<Cow> cow = cowRepository.findByOwnerIdAndId(ownerId, petId);
        if(cow.isPresent()) {
            cowRepository.delete(cow.get());
            return new OwnerResponse("Cow deleted successfully.");
        }else {
            throw new ResourceNotFoundException("No cow available for delete.");
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public Cow editCow(Cow cow) {
        System.out.println("Get cow id:"+cow.getId());
        Optional<Cow> cow1 = cowRepository.findByOwnerIdAndId(cow.getOwnerId(), cow.getId());

        if(cow1.isPresent()) {
            cow1.get().setBreed(cow.getBreed());
            cow1.get().setDateOfBirth(cow.getDateOfBirth());
            cow1.get().setSex(cow.getSex());
            //cow1.get().setEarTagNumber(cow.getEarTagNumber());
            cow1.get().setCphNumber(cow.getCphNumber());
            cow1.get().setBodyTemperature(cow.getBodyTemperature());
            cow1.get().setDateOfDeath(cow.getDateOfDeath());
            cow1.get().setMovementFrom(cow.getMovementFrom());
            cow1.get().setMovementTo(cow.getMovementTo());
            cow1.get().setDateOfMovement(cow.getDateOfMovement());
            cow1.get().setPassportNo(cow.getPassportNo());

            cow1.get().setDevice(cow.getDevice());


            if(cow.getDevice() != null) {
                Optional<Cow> cow2 = cowRepository.findByOwnerIdAndDeviceDeviceId(cow.getOwnerId(), cow.getDevice().getDeviceId());
                Optional<Sheep> sheep = sheepRepository.findByOwnerIdAndDeviceDeviceId(cow.getOwnerId(), cow.getDevice().getDeviceId());
                if(cow2.isPresent()){
                    cow2.get().setDevice(null);
                    cowRepository.save(cow2.get());

                }else if(sheep.isPresent()){
                    sheep.get().setDevice(null);
                    sheepRepository.save(sheep.get());
                }
            }

            cowRepository.save(cow1.get());
            return cow1.get();

        }else {
            throw new ResourceNotFoundException("No cow available for update.");
        }
    }
}
