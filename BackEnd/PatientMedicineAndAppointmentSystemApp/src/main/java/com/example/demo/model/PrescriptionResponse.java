package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionResponse {
	private int prescriptionId;
	
	private String doctorName;

	private String patientName;

	private List<MedicationResponse> medications;

	private LocalDate prescriptionDate;
}
