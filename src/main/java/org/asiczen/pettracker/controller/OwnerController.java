package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.OwnerDeviceListUpdateReq;
import org.asiczen.pettracker.dto.response.ApiResponse;
import org.asiczen.pettracker.dto.response.CountResponse;
import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Device;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.service.CattleService;
import org.asiczen.pettracker.service.OwnerService;
import org.asiczen.pettracker.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owner")
@Slf4j
public class OwnerController {

        @Autowired
        OwnerService ownerService;

        @Autowired
        PetService petService;

        @Autowired
        CattleService cattleService;

        @PatchMapping("/editDevice")
        public OwnerResponse updateDeviceList(@RequestBody OwnerDeviceListUpdateReq ownerDeviceListUpdateReq) {
            return ownerService.updateDeviceList(ownerDeviceListUpdateReq);
        }

        @ResponseStatus(HttpStatus.OK)
        @GetMapping("/getAllDeviceList")
        public List<Device> getDeviceList(@Valid @RequestParam String ownerId) {
            return ownerService.getAllDeviceList(ownerId);
        }

        @ResponseStatus(HttpStatus.OK)
        @DeleteMapping("/deleteDevice")
        public OwnerResponse deleteDevice(@Valid @RequestParam String ownerId, @RequestParam String devEui) {
                return ownerService.deleteDevice(ownerId, devEui);
        }

        @ResponseStatus(HttpStatus.OK)
        @GetMapping("/getOwner")
        public Owner getOwnerDetails(@Valid @RequestParam String ownerId) {
                return ownerService.getOwnerDetails(ownerId);
        }

        @ResponseStatus(HttpStatus.OK)
        @GetMapping("/getDeviceAndPetAndCattleCount")
        public ResponseEntity<?> getDeviceCount(@Valid @RequestParam String ownerId) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse(HttpStatus.OK.value(), "Owner device and pet count extracted successfully",
                                new CountResponse(ownerService.getDeviceCount(ownerId),petService.getPetCount(ownerId),cattleService.getCattleCount(ownerId))));
        }




}
