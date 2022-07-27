package org.asiczen.pettracker.serviceImpl.message;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.dto.response.AlertMessageResponse;
import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.OwnerConfig;
import org.asiczen.pettracker.model.message.GeofenceLocation;
import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.asiczen.pettracker.repository.GeofenceLocationsRepository;
import org.asiczen.pettracker.repository.OwnerConfigRepository;
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

import java.util.Arrays;
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

    @Autowired
    GeofenceLocationsRepository geofenceLocationsRepository;

    @Override
    public void geoFenceViolation(TransFormedMessage transFormedMessage) {
        Optional<OwnerConfig> ownerConfig = ownerConfigRepository.findByOwnerId(transFormedMessage.getOwnerId());

        if (ownerConfig.isPresent()) {
            OwnerConfig ownerConfig1 = ownerConfig.get();
            if (ownerConfig1.isAlertOn() && ownerConfig1.isGeoFenceAlertOn()) {

                GeofenceLocation geofenceLocation = geofenceLocationsRepository.findByOwnerId(transFormedMessage.getOwnerId());

                //Point[] points = new Point[]{geofenceLocation.getPointList()[0]};


                int count = (int) Arrays.stream(geofenceLocation.getPointList()).count();

                Point[] points = new Point[count];

                //points = geofenceLocation.getPointList()

                for (int i = 0;i<count;i++) {

                    points[i] = new Point(geofenceLocation.getPointList()[i].getX(),geofenceLocation.getPointList()[i].getY());

                }

                boolean insideOrNot = isInside(points,count, new Point(transFormedMessage.getLatitude(),transFormedMessage.getLongitude()));

                if(insideOrNot) {

                    System.out.println("Pet inside geofence");

                }else {
                    sendToSocket(transFormedMessage);

                    sendsms(transFormedMessage);
                }

               /* double distanceCal = distance(ownerConfig1.getLat(),ownerConfig1.getLng(),transFormedMessage.getLatitude(),transFormedMessage.getLongitude(),"K");
                if (ownerConfig1.getRadiusInMtr() < distanceCal) {

                    sendToSocket(transFormedMessage);

                    sendsms(transFormedMessage);

                }*/
            }

        }else {
            throw new ResourceNotFoundException("Invalid owner id to get owner config.");
        }
    }

    @Override
    public void temperatureAlert(TransFormedMessage transFormedMessage) {

    }

    @Override
    public void lowBatteryAlert(TransFormedMessage transFormedMessage) {

    }

    @Override
    public void cattleNoMovementAlert(TransFormedMessage transFormedMessage) {

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

        String alertMsg = "Your pet "+ transFormedMessage.getCow().getEarTagNumber() + " has gone out of fence.";

        alertMessageResponse.setMessage(alertMsg);
        alertMessageResponse.setOwnerId(transFormedMessage.getOwnerId());
        alertMessageResponse.setDevEui(transFormedMessage.getDevEui());
        alertMessageResponse.setLatitude(transFormedMessage.getLatitude());
        alertMessageResponse.setLongitude(transFormedMessage.getLongitude());
        alertMessageResponse.setTimeStamp(transFormedMessage.getTimeStamp());

        alertMessageResponse.setAnimalType(transFormedMessage.getAnimalType());
        alertMessageResponse.setCow(transFormedMessage.getCow());
        alertMessageResponse.setSheep(transFormedMessage.getSheep());

        iotMessageSource.petAlertMessageSource().send(MessageBuilder.withPayload(alertMessageResponse).build());
    }

    //Geofence calculation Using New Logic


    // Define Infinite (Using INT_MAX
    // caused overflow problems)
    static int INF = 10000;

    static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    ;

    // Given three collinear points p, q, r,
    // the function checks if point q lies
    // on line segment 'pr'
    static boolean onSegment(Point p, Point q, Point r) {
        if (q.x <= Math.max(p.x, r.x) &&
                q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) &&
                q.y >= Math.min(p.y, r.y)) {
            return true;
        }
        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are collinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    static int orientation(Point p, Point q, Point r) {
        double val = (q.y - p.y) * (r.x - q.x)
                - (q.x - p.x) * (r.y - q.y);

        if (val == 0) {
            return 0; // collinear
        }
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // The function that returns true if
    // line segment 'p1q1' and 'p2q2' intersect.
    static boolean doIntersect(Point p1, Point q1,
                               Point p2, Point q2) {
        // Find the four orientations needed for
        // general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special Cases
        // p1, q1 and p2 are collinear and
        // p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        // p1, q1 and p2 are collinear and
        // q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        // p2, q2 and p1 are collinear and
        // p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        // p2, q2 and q1 are collinear and
        // q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) {
            return true;
        }

        // Doesn't fall in any of the above cases
        return false;
    }

    // Returns true if the point p lies
    // inside the polygon[] with n vertices
    static boolean isInside(Point polygon[], int n, Point p) {

        // There must be at least 3 vertices in polygon[]
        if (n < 3) {
            return false;
        }

        // Create a point for line segment from p to infinite
        Point extreme = new Point(INF, p.y);

        // Count intersections of the above line
        // with sides of polygon
        int count = 0, i = 0;
        do {
            int next = (i + 1) % n;

            // Check if the line segment from 'p' to
            // 'extreme' intersects with the line
            // segment from 'polygon[i]' to 'polygon[next]'
            if (doIntersect(polygon[i], polygon[next], p, extreme)) {
                // If the point 'p' is collinear with line
                // segment 'i-next', then check if it lies
                // on segment. If it lies, return true, otherwise false
                if (orientation(polygon[i], p, polygon[next]) == 0) {
                    return onSegment(polygon[i], p,
                            polygon[next]);
                }

                count++;
            }
            i = next;
        } while (i != 0);

        // Return true if count is odd, false otherwise
        return (count % 2 == 1); // Same as (count%2 == 1)

    }

}
