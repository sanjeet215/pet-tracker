package org.asiczen.pettracker.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.exception.ResourceAlreadyExistException;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.repository.OwnerRepository;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    PetRepository petRepository;

    @Override
    public OwnerResponse updateDeviceList(OwnerDeviceListUpdateReq ownerDeviceListUpdateReq) {


        Owner owner = ownerRepository.findByOwnerId(ownerDeviceListUpdateReq.getOwnerId());
        if (owner != null) {

            if (owner.getDeviceList() != null && owner.getDeviceList().contains(ownerDeviceListUpdateReq.getDevice())) {

                throw new ResourceAlreadyExistException("Already this device available");
            } else {
                if (owner.getDeviceList() != null) {
                    owner.getDeviceList().add(ownerDeviceListUpdateReq.getDevice());
                } else {
                    owner.setDeviceList(Collections.singletonList(ownerDeviceListUpdateReq.getDevice()));
                }
                try {
                    ownerRepository.save(owner);
                } catch (Exception exception) {
                    log.error("Update error");
                }
                return new OwnerResponse("Device registered successfully");
            }

        } else {
            throw new ResourceNotFoundException("Invalid owner id to get owner.");
        }
    }

    @Override
    public List<Device> getAllDeviceList(String ownerId) {

        Owner owner = ownerRepository.findByOwnerId(ownerId);
        if (owner != null) {

            return owner.getDeviceList() != null ? owner.getDeviceList() : new ArrayList<>();

        } else {
            throw new ResourceNotFoundException("Invalid owner id to get owner.");
        }

    }

    @Override
    public Owner getOwnerDetails(String ownerId) {

        Owner owner = ownerRepository.findByOwnerId(ownerId);
        if (owner != null) {

            return owner;

        } else {
            throw new ResourceNotFoundException("Invalid owner id to get owner.");
        }
    }

    @Override
    public long getDeviceCount(String ownerId) {
        Owner owner = ownerRepository.findByOwnerId(ownerId);
        if(owner != null) {
            return (owner.getDeviceList() != null) ? owner.getDeviceList().size() : 0L;
        }else {
            throw new ResourceNotFoundException("Invalid owner id to get device count.");
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public OwnerResponse deleteDevice(String ownerId, String devEui) {
        Owner owner = ownerRepository.findByOwnerId(ownerId);
        if (owner != null) {

            if (owner.getDeviceList() != null){

               Device device = owner.getDeviceList().stream().filter(device1 -> device1.getDeviceId().equals(devEui)).findAny().orElse(null);
               log.info("Before delete {}",device);
               if(device != null) {
                   if(owner.getDeviceList().remove(device)) {

                      removeDeviceFromPet(ownerId, devEui);
                      return new OwnerResponse("Device deleted successfully.");

                   } else {
                       return new OwnerResponse("Device not deleted.");
                   }

               } else {
                   throw new ResourceNotFoundException("Invalid owner id to get owner.");
               }


            }else {
                throw new ResourceNotFoundException("No device available for delete.");
            }
            //return owner.getDeviceList() != null ? owner.getDeviceList() : new ArrayList<>();

        } else {
            throw new ResourceNotFoundException("Invalid owner id to get owner.");
        }
    }

    private void removeDeviceFromPet(String ownerId, String devEui) {
        Optional<Pet> pet = petRepository.findByOwnerIdAndDeviceDeviceId(ownerId, devEui);

        if (pet.isPresent()) {

            Pet pet1 = pet.get();
            pet1.setDevice(null);
            petRepository.save(pet1);



        }else {
            throw new ResourceNotFoundException("Invalid owner and device id to get pet.");
        }
    }
}
