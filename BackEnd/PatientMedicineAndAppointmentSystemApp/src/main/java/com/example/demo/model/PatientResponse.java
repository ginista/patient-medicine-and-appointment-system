package com.example.demo.model;

import com.example.demo.model.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponse {

	private int patientId;
	private String name;
	private Gender gender;
	private String bloodGroup;
	private String contactNumber;
	private int age;
	private String address;

}
