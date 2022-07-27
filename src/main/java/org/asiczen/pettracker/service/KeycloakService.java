package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface KeycloakService {

    public String createUserInKeyCloak(UserDto userDTO, String token);
}
