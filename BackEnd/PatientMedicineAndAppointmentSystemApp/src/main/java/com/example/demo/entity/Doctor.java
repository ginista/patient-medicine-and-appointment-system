package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctor")
@Getter
@Setter
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doctor_id")
	private int doctorId;

	@Column(name = "doctor_name")
	private String doctorName;

	@Column
	private String email;

	@Column
	private String specialist;

	@Column
	private int experience;

	@Column
	private int age;

	@Column(name = "phone_number")
	private String phoneNumber;


	@Column(name="contact_address")
	private String contactAddress;


}
