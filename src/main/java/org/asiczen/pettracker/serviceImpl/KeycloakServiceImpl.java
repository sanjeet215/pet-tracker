package org.asiczen.pettracker.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.UserDto;
import org.asiczen.pettracker.exception.AccessisDeniedException;
import org.asiczen.pettracker.exception.InternalServerError;
import org.asiczen.pettracker.exception.ResourceAlreadyExistException;
import org.asiczen.pettracker.model.Owner;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.repository.OwnerConfigRepository;
import org.asiczen.pettracker.repository.OwnerRepository;
import org.asiczen.pettracker.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${app.url.user}")
    private String USERADDURL;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OwnerConfigRepository ownerConfigRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createUserInKeyCloak(UserDto request, String token) {

        String returnResponse = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token);

        log.debug("token --> {}", token);

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("userName", request.getUserName());
        requestBody.put("firstName", request.getFirstName());
        requestBody.put("lastName", request.getLastName());
        requestBody.put("orgRefName", request.getOrgRefName());
        requestBody.put("contactNumber", request.getContactNumber());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response;

        try {
            response = restTemplate.postForEntity(USERADDURL + "/createuser", requestEntity, String.class);

            log.trace(response.getStatusCode().toString());
            log.info(response.getHeaders().toString());
            log.info(response.getBody());
            log.trace(response.toString());

            if (response.getStatusCodeValue() == 201) {
                try {
                    createOwner(request,response.getBody());
                    returnResponse = "User created successfully for pet.";
                } catch (Exception exception) {
                    log.info("after create user");
                    log.error("Error while sending email/sms. Please contact system admin.");
                    returnResponse = "user created successfully but there is problem while sending credentials to user.";
                }

            } else {
                returnResponse = "some issue while creating user , please try again in some time. If the issue persists please contact system admin.";
            }

            return returnResponse;

        } catch (HttpClientErrorException.Conflict cep) {
            log.error("User already present in the system with {} user name", request.getUserName());
            log.error(cep.getMessage());
            throw new ResourceAlreadyExistException("user name already registered in system " + request.getUserName());

        } catch (HttpClientErrorException.Forbidden ep) {
            log.error("Resource is forbidden for user {}", request.getUserName());
            throw new AccessisDeniedException("Access is denied");
        } catch (HttpClientErrorException.Unauthorized ep) {
            throw new AccessisDeniedException("Access is denied");
        } catch (Exception ep) {
            log.error("Error occurred : {}", ep.getLocalizedMessage());
            throw new InternalServerError(ep.getLocalizedMessage());
        }

    }

    public void createOwner(UserDto request,String ownerId) {

        Owner owner = new Owner();
        owner.setOwnerId(ownerId);

        try {
            ownerRepository.save(owner);
            setOwnerConfig(request,ownerId);
        }catch (Exception e) {
            log.error("Error in save owner {}",e.getLocalizedMessage());
        }

    }

    public void setOwnerConfig(UserDto request,String ownerId) {

        OwnerConfig ownerConfig = new OwnerConfig();

        ownerConfig.setOwnerId(ownerId);
        ownerConfig.setContactNumber(request.getContactNumber());
        try {
            ownerConfigRepository.save(ownerConfig);
        }catch (Exception exception) {
            log.error("Error in set owner config {}",exception.getLocalizedMessage());
        }

    }

}
