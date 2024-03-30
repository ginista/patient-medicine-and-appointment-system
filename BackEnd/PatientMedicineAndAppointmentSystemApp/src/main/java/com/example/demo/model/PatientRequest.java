package com.example.demo.model;

import com.example.demo.model.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Gender is required")
	private Gender gender;

	@NotBlank(message = "Blood group is required")
	private String bloodGroup;

	@Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
	private String contactNumber;

	private int age;

	@NotBlank(message = "address is required")
	private String address;

}
