package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.model.Device;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OwnerService {
    void updateDeviceList(OwnerDeviceListUpdateReq ownerDeviceListUpdateReq);

    List<Device> getAllDeviceList(String ownerId);
}
