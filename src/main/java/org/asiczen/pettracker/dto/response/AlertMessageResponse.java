package org.asiczen.pettracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.Pet;

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

    private String animalType;

    private String message;

    private Pet pet;

    private Cattle cattle;
}
