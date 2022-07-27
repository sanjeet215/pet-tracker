package org.asiczen.pettracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceConfigRequest {

    private String ownerId;

    private long radiusInMtr;

    private double lat;

    private double lng;

    private String contactNumber;
}
