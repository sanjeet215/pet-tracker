package org.asiczen.pettracker.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {

	private static final long serialVersionUID = -4144288653555563016L;

	@Email
	@NotEmpty(message = "User Name field is mandatory")
	private String userName;

	@NotEmpty(message = "First Name is required/Can't be blank")
	@Size(min = 1, max = 16, message = "First Name should be between 1 to 16 characters")
	private String firstName;

	@NotEmpty(message = "Last Name is required/Can't be blank")
	@Size(min = 1, max = 16, message = "Last Name should be between 1 to 16 characters")
	private String lastName;

	@NotEmpty(message = "OrgRefName is mandatory/can't be blank")
	private String orgRefName;

	@NotEmpty(message = "contactNumber is mandatory/Can't be blank")
	private String contactNumber;

//	@NotEmpty(message = "Password is mandatory/Can't be blank")
//	private String password;
}
