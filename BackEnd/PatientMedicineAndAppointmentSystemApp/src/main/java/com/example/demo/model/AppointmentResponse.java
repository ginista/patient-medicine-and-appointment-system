package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.model.enums.AppointmentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponse {

	private int appointmentId;

	private int tokenNumber;

	private String patientName;

	private int consultationId;
	
	private LocalDate slotDate;

	private LocalTime fromTime;

	private LocalTime toTime;

	private double consultationCharge;

	private String doctorName;

	private String problem;

	private int prescriptionId;

	private LocalDate appointmentBookedDate;

	private AppointmentStatus appointmentStatus;
}
