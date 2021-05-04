package org.asiczen.pettracker.serviceImpl.message;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.AlertMessageResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.asiczen.pettracker.repository.OwnerConfigRepository;
import org.asiczen.pettracker.repository.PetRepository;
import org.asiczen.pettracker.repository.TransFormedMessageRepository;
import org.asiczen.pettracker.service.message.AlertProcessService;
import org.asiczen.pettracker.source.IOTMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
@Slf4j
@EnableBinding(IOTMessageSource.class)
public class AlertProcessServiceImpl implements AlertProcessService {

    @Value("${sms.api.url}")
    private String APIURL;

    @Value("${sms.api.apikey}")
    private String APIKEY;

    @Value("${sms.api.SenderID}")
    private String SENDERID;

    @Value("${sms.api.ServiceName}")
    private String SERVICENAME;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IOTMessageSource iotMessageSource;

    @Autowired
    OwnerConfigRepository ownerConfigRepository;

    @Override
    public void geoFenceViolation(TransFormedMessage transFormedMessage) {
        Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(transFormedMessage.getOwnerId());

        if (ownerConfig.isPresent()) {
            OwnerConfig ownerConfig1 = ownerConfig.get();
            if (ownerConfig1.isAlertOn() && ownerConfig1.isGeoFenceAlertOn()) {

                double distanceCal = distance(ownerConfig1.getLat(),ownerConfig1.getLng(),transFormedMessage.getLatitude(),transFormedMessage.getLongitude(),"K");
                if (ownerConfig1.getRadiusInMtr() < distanceCal) {

                    sendToSocket(transFormedMessage);

                    sendsms(transFormedMessage);

                }
            }

        }else {
            throw new ResourceNotFoundException("Invalid owner id to get owner config.");
        }
    }


    //Calculate distance between 2 point
    public double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equalsIgnoreCase("K")) {
            dist = dist * 1.609344;
        } else if (unit.equalsIgnoreCase("N")) {
            dist = dist * 0.8684;
        }
        return (dist*1000);
    }

    //This function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //This function converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private void sendsms(TransFormedMessage message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(APIURL).queryParam("APIKEY", APIKEY)
                .queryParam("MobileNo", ownerConfigRepository.findByOwnerId(message.getOwnerId()).get().getContactNumber()).queryParam("SenderID", SENDERID)
                .queryParam("Message", buildMessage(message)).queryParam("ServiceName", SERVICENAME);

        try {
            log.info("Message Builder uri : {}",uriBuilder);
            log.info("Full Message  : {}",uriBuilder.toUriString().replace("%20", " "));
            ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.toUriString().replace("%20", " "),
                    String.class, null, headers);

            if (response.getStatusCode().value() != 200) {
                log.error("There is some issue while sending the message, please check the logs");
                log.error(response.getBody());
            } else {
                log.info("SMS sent successfully");
                log.info("Response is --> {}", response.toString());
            }

        } catch (Exception ep) {
            log.error("There is an error while publishing the message. {} ", ep.getMessage());
            ep.getStackTrace();
        }

    }

    private String buildMessage(TransFormedMessage message) {

        String alertMessage = null;

        alertMessage =  "Dear ASICZEN User, Your pet has gone out of fence, at location " +"("+ message.getLatitude()+ "," + message.getLongitude() +")"+ ".";

        return alertMessage;
    }

    private void sendToSocket(TransFormedMessage transFormedMessage) {

        AlertMessageResponse alertMessageResponse = new AlertMessageResponse();

        String alertMsg = "Your pet "+ transFormedMessage.getPetName() + " has gone out of fence.";

        alertMessageResponse.setMessage(alertMsg);
        alertMessageResponse.setOwnerId(transFormedMessage.getOwnerId());
        alertMessageResponse.setDevEui(transFormedMessage.getDevEui());
        alertMessageResponse.setLatitude(transFormedMessage.getLatitude());
        alertMessageResponse.setLongitude(transFormedMessage.getLongitude());
        alertMessageResponse.setPetName(transFormedMessage.getPetName());
        alertMessageResponse.setPetBreed(transFormedMessage.getPetBreed());
        alertMessageResponse.setPetType(transFormedMessage.getPetType());
        alertMessageResponse.setTimeStamp(transFormedMessage.getTimeStamp());

        iotMessageSource.petAlertMessageSource().send(MessageBuilder.withPayload(alertMessageResponse).build());
    }

}
