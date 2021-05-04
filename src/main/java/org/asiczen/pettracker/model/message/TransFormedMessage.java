package org.asiczen.pettracker.model.message;

import lombok.*;
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

    private String petName;

    private String petType;

    private String petBreed;
}
