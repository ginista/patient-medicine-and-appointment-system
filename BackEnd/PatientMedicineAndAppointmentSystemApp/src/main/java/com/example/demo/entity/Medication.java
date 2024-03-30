package com.example.demo.entity;

import jakarta.persistence.CascadeType;
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
@Table(name = "medications")
@Getter
@Setter
public class Medication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "medication_id")
	private int id;

	@Column(name = "medication_name")
	private String medicationName;

	@Column
	private String dosage;

	@Column
	private String frequency;

	@Column
	private String instructions;

	@Column(name = "no_of_days")
	private int noOfDays;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="prescription_id")
	private Prescription prescription;

}
