package org.asiczen.pettracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.asiczen.pettracker.model.Device;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OwnerDeviceListUpdateReq {

    private String ownerId;
    private Device device;
}
