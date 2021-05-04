package org.asiczen.pettracker.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OriginalMessage {

    private EndDeviceIDs end_device_ids;

    private Date received_at;

    private SolvedLocation location_solved;
}
