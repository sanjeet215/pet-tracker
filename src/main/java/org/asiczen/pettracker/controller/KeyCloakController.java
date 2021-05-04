package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.UserDto;
import org.asiczen.pettracker.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/createowner")
@Slf4j
public class KeyCloakController {

    @Autowired
    KeycloakService keycloakService;

    @PostMapping(value = "/createuser")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUserinKeyCloak(@Valid @RequestBody UserDto userDto, @RequestHeader String Authorization) {

        log.trace("Token is --->" + Authorization);
        log.trace("Request object --> {}", userDto.toString());
        log.info("Request object --> {}", userDto.toString());

        return new ResponseEntity<>(keycloakService.createUserInKeyCloak(userDto, Authorization), HttpStatus.CREATED);
    }
}
