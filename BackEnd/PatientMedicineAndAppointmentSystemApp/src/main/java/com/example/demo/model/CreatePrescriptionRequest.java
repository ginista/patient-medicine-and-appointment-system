package com.example.demo.model;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePrescriptionRequest {

	@NotNull(message = "Appointment Id is required")
	private int appointmentId;

	@NotNull(message = "Doctor Id is required")
	private int doctorId;

	@NotNull(message = "Missing content in request body")
	@Size(min = 1, message = "Atleast 1 medication is required for creating a prescription")
	private List<MedicationRequest> medicationRequests;

}
