package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationRequest {

	@NotBlank(message = "Name is required")
	private String medicationName;

	@NotBlank(message ="dosage is required")
	private String dosage;

	@NotBlank(message = "frequency is required")
	private String frequency;

	@NotBlank(message="Instruction shoud be given")
	private String instructions;

	@NotNull(message = "Number of days is required")
	private int noOfDays;

}
