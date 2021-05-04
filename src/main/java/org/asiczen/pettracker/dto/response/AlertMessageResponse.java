package org.asiczen.pettracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlertMessageResponse {

    private String ownerId;

    private String devEui;

    private double latitude;

    private double longitude;

    private Date timeStamp;

    private String petName;

    private String petType;

    private String petBreed;

    private String message;
}
