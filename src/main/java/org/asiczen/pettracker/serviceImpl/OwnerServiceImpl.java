package org.asiczen.pettracker.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.repository.OwnerRepository;
import org.asiczen.pettracker.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    OwnerRepository ownerRepository;

    @Override
    public String updateDeviceList(OwnerDeviceListUpdateReq ownerDeviceListUpdateReq) {


        Owner owner = ownerRepository.findByOwnerId(ownerDeviceListUpdateReq.getOwnerId());
        if (owner != null) {

            if(owner.getDeviceList().contains(ownerDeviceListUpdateReq.getDevice())) {

                return "Already this device available";
            }else{
                owner.getDeviceList().add(ownerDeviceListUpdateReq.getDevice());
                try {
                    ownerRepository.save(owner);
                } catch (Exception exception) {
                    log.error("Update error");
                }
                return "Device registered successfully";
            }

        } else {
            throw new ResourceNotFoundException("Invalid owner id to get owner.");
        }
    }

    @Override
    public List<Device> getAllDeviceList(String ownerId) {

        Owner owner = ownerRepository.findByOwnerId(ownerId);
        if (owner != null) {

            return owner.getDeviceList();

        } else {
            throw new ResourceNotFoundException("Invalid owner id to get owner.");
        }

    }
}
