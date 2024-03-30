package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Patient;
import com.example.demo.model.AppointmentRequest;
import com.example.demo.model.AppointmentResponse;
import com.example.demo.model.ConsultationResponse;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/appointment")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/available")
    public ResponseEntity<List<ConsultationResponse>> findAvailableSlotsByDate(
            @RequestParam("date")  LocalDate selectedDate) {
        List<ConsultationResponse> availableSlots = appointmentService.findAvailableSlotsByDate(selectedDate);
        return ResponseEntity.ok(availableSlots);
    }

	
	@PostMapping("/book")
	public ResponseEntity<AppointmentResponse> bookAppointment(
			@NotNull(message="Missing request body") @Valid
			@RequestBody AppointmentRequest appointmentRequest,
			Authentication authentication) {
		Patient patient = userService.getPatientByUserName(authentication.getName());
		AppointmentResponse appointmentResponse = appointmentService.bookAppointment(patient,appointmentRequest);
		return ResponseEntity.ok(appointmentResponse);
	}
	
	@GetMapping
	public ResponseEntity<List<AppointmentResponse>> findByPatientId(Authentication authentication){
		Patient patient = userService.getPatientByUserName(authentication.getName());
		List<AppointmentResponse> appointmentResponse = appointmentService.findByPatientId(patient.getPatientId());
		return ResponseEntity.ok(appointmentResponse);
	}
	
	

}
