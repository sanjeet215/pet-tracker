package org.asiczen.pettracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "address_pet")
public class Address {

    @Id
    private String id;
    private String addressLine1;
    private String addressLine2;
    private String city;

    @Indexed
    private String state;

    @Indexed
    private String country;

    @Indexed
    private String zipCode;

}
