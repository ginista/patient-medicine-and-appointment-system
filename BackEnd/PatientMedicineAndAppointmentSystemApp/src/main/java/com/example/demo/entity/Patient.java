package com.example.demo.entity;

import com.example.demo.model.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patient")
@Getter
@Setter
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "patient_id")
	private int patientId;

	@Column
	private String name;

	@Column
	private Gender gender;

	@Column(name = "blood_group")
	private String bloodGroup;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column
	private int age;

	@Column
	private String address;
	
}
