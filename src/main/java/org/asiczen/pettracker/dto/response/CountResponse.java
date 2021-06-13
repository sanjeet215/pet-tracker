package org.asiczen.pettracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CountResponse {

    private long deviceCount;

    private long petCount;

    private long cattleCount;
}
