package org.asiczen.pettracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "ownerconfig")
@EqualsAndHashCode(callSuper = false)
public class OwnerConfig {

    @Id
    private String id;

    @Indexed(unique = true)
    private String ownerId;

    private long radiusInMtr = 0;

    private double lat = 0.0;

    private double lng = 0.0;

    private boolean isGeoFenceAlertOn = false;

    private boolean isAlertOn = false;

    private String contactNumber;

}
