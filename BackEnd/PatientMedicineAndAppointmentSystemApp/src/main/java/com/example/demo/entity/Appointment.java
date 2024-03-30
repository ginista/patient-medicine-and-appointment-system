package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.model.enums.AppointmentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "appointment_id")
	private int appointmentId;

	@ManyToOne
	@JoinColumn(name = "patientId")
	private Patient patient;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="consultation_id")
	private Consultation consultation;

	private String problem;

	@OneToOne
	@JoinColumn(name = "prescription_id")
	private Prescription prescription;

	@Column(name = "appointment_booked_date")
	private LocalDate appointmentBookedDate;
	
	private int tokenNumber;

	@Column(name = "appointment_status")
	private AppointmentStatus appointmentStatus;

}
