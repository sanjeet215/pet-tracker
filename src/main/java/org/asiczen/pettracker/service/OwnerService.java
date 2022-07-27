package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OwnerService {
    OwnerResponse updateDeviceList(OwnerDeviceListUpdateReq ownerDeviceListUpdateReq);

    List<Device> getAllDeviceList(String ownerId);

    Owner getOwnerDetails(String ownerId);

    long getDeviceCount(String ownerId);

    OwnerResponse deleteDevice(String ownerId,String devEui);
}
