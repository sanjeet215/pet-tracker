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
@Document(collection = "cow_cow")
public class Cow {

    @Id
    private String id;
    @Indexed
    private String ownerId;
    private String animalType = "cow";
    private String earTagNumber;
    private String passportNo;
    private Date dateOfBirth;
    private String breed;
    private String sex;
    private String bodyTemperature;
    private String cphNumber;
    private Date dateOfMovement;
    private String movementFrom;
    private String movementTo;
    private Date dateOfDeath;

    //@DBRef
    private Device device; // One-to-one with device.
}
