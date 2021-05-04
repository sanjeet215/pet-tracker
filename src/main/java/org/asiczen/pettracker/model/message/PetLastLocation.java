package org.asiczen.pettracker.model.message;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "petlastlocationdtl")
@EqualsAndHashCode(callSuper = false)
public class PetLastLocation {

    @Id
    private String id;

    private String ownerId;

    @Indexed(unique = true)
    private String devEui;

    private double latitude;

    private double longitude;

    private Date timeStamp;

    private String petName;

    private String petType;

    private String petBreed;

}
