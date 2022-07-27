package org.asiczen.pettracker.model.message;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "geofencelocations")
@EqualsAndHashCode(callSuper = false)
public class GeofenceLocation {

    @Id
    private String id;

    private String ownerId;

    private Points[] pointList;

}
