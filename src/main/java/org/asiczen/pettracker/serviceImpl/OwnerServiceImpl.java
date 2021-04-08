package org.asiczen.pettracker.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.exception.ResourceAlreadyExistException;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.repository.OwnerRepository;
import org.asiczen.pettracker.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    OwnerRepository ownerRepository;

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
}
