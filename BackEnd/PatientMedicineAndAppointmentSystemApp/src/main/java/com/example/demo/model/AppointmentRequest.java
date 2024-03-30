package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AppointmentRequest {

	@NotNull(message = "Consultation Id is required")
	private int consultationId;

	@NotBlank(message = "Problem description is required")
	private String problem;

}
