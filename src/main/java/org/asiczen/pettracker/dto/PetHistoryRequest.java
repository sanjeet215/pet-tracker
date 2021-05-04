package org.asiczen.pettracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PetHistoryRequest {

    @NotNull(message = "device number cannot be missing or empty")
    String devEui;


    Date startDateTime;
    Date endDateTime;
}
