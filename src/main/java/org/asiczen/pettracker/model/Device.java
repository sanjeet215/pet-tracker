package org.asiczen.pettracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "device_pet")
@Builder
public class Device {
    @Id
    private String id;
    @Indexed(unique = true)
    private String deviceId;
    private String deviceType;
}
