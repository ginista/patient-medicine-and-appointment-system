package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationResponse {
	
	private int medicationId;

	private String medicationName;

	private String dosage;

	private String frequency;

	private String instructions;

	private int noOfDays;

}
