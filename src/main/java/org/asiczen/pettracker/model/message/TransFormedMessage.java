package org.asiczen.pettracker.model.message;

import lombok.*;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.Pet;
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

    private Pet pet;

    private Cattle cattle;

}
