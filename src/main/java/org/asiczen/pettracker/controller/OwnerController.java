package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
@Slf4j
public class OwnerController {

        @Autowired
        OwnerService ownerService;

        @PatchMapping("/device")
        public OwnerResponse updateDeviceList(@RequestBody OwnerDeviceListUpdateReq ownerDeviceListUpdateReq) {
            return ownerService.updateDeviceList(ownerDeviceListUpdateReq);
        }

        @ResponseStatus(HttpStatus.OK)
        @GetMapping("/device")
        public List<Device> getDeviceList(@Valid @RequestParam String ownerId) {
            return ownerService.getAllDeviceList(ownerId);
        }

        @ResponseStatus(HttpStatus.OK)
        @GetMapping
        public Owner getOwnerDetails(@Valid @RequestParam String ownerId) {
                return ownerService.getOwnerDetails(ownerId);
        }




}
