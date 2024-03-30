package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "consultation")
@Getter
@Setter
public class Consultation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consultation_id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@Column(name = "slot_date")
	private LocalDate slotDate;

	@Column(name = "from_time")
	private LocalTime fromTime;

	@Column(name = "to_time")
	private LocalTime toTime;

	@Column(name = "consultation_charge")
	private double consultationCharge;

	@Column(name = "total_tokens")
	private int totalTokens;

	@Column(name = "available_tokens")
	private int availableTokens;

	@Column(name = "last_booked_token_number")
	private int lastBookedTokenNumber;

}
