package org.asiczen.pettracker.model.message;


import lombok.*;
import org.asiczen.pettracker.model.Cow;
import org.asiczen.pettracker.model.Sheep;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "petalerttemperature")
@EqualsAndHashCode(callSuper = false)
public class PetTemperatureCalculate {

    private String ownerId;

    private String devEui;

    private Date timeStamp;

    private double temperature;

    private double battery;

}
