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
@Document(collection = "petmessagesprod")
@EqualsAndHashCode(callSuper = false)
public class TransFormedMessage {

    private String ownerId;

    private String devEui;

    private double latitude;

    private double longitude;

    private Date timeStamp;

    private String animalType;

    private String temperature;

    private double battery;

    private Cow cow;

    private Sheep sheep;

}
