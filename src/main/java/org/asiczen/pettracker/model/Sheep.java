package org.asiczen.pettracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sheep")
public class Sheep {

    @Id
    private String id;
    @Indexed
    private String ownerId;
    private String animalType = "sheep";
    private String earTagNumber;
    private Date dateOfBirth;
    private String bodyTemperature;
    private Date dateOfMovement;
    private String movementFrom;
    private String movementTo;
    private Date dateOfDeath;

    private Device device;
}
