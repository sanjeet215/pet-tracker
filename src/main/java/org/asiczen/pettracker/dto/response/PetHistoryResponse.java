package org.asiczen.pettracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PetHistoryResponse {

	private String devEui;
	private List<Location> locationlist;

	private Date timeStamp;
}
