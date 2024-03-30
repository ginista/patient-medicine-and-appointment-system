package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultationResponse {

	private int consultationId;
	
	private String doctorName;
	
	private String doctorSpecialization;
	
	private  LocalDate slotDate;
	
	private LocalTime fromTime;
	
	private LocalTime toTime;
	
	private double consultationCharge;
	
	private int totalTokens;
	
	private int availableTokens;
	
	private int lastBookedTokenNumber;
}
