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
@Document(collection = "cattle")
public class Cattle {

    @Id
    private String id;
    @Indexed
    private String ownerId;
    private String animalType = "cattle";
    private String earTagNumber;
    private Date dob;
    private String breed;
    private String sex;
    private String geneticDam;
    private String damIdNumber;
    private Date dateOfPassportIssue;
    //If valid date entered the device stop working
    private Date dateOfCattleDied;


    private Device device;

}
