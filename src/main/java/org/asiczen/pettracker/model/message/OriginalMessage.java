package org.asiczen.pettracker.model.message;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OriginalMessage {

    private String temperature;

    private String voltage;

    private EndDeviceIDs end_device_ids;

    private Date received_at;

    private SolvedLocation location_solved;

}
