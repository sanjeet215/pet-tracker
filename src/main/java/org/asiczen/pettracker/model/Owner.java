package org.asiczen.pettracker.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ownerDetails_pet")
public class Owner {

    @Indexed
    private String ownerId;
    private List<Device> deviceList;
    private Address address;
}
