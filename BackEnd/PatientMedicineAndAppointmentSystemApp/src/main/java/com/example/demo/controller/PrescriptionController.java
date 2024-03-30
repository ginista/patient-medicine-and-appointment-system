package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Patient;
import com.example.demo.model.CreatePrescriptionRequest;
import com.example.demo.model.MedicationRequest;
import com.example.demo.model.MedicationResponse;
import com.example.demo.model.PrescriptionResponse;
import com.example.demo.service.PrescriptionService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("prescriptions")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class PrescriptionController {

	@Autowired
	private PrescriptionService prescriptionService;

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<PrescriptionResponse> createPrescriptionWithMedications(Authentication authentication,
			@RequestBody @Valid @NotNull(message = "Request Body is missing") CreatePrescriptionRequest createPrescriptionRequest) {

		Patient patient = userService.getPatientByUserName(authentication.getName());

		PrescriptionResponse createdPrescription = prescriptionService.createPrescriptionWithMedications(patient,
				createPrescriptionRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPrescription);
	}

	@GetMapping
	public ResponseEntity<List<PrescriptionResponse>> getPrescriptions(Authentication authentication) {
		Patient patient = userService.getPatientByUserName(authentication.getName());

		List<PrescriptionResponse> prescriptions = prescriptionService.getPrescriptions(patient.getPatientId());

		return ResponseEntity.ok(prescriptions);
	}

	@PostMapping("{prescriptionId}/medications")
	public ResponseEntity<MedicationResponse> addMedication(Authentication authentication,
			@PathVariable("prescriptionId") int prescriptionId,
			@Valid @RequestBody @NotNull(message = "Missing content in request body") MedicationRequest medicationRequest) {
		Patient patient = userService.getPatientByUserName(authentication.getName());

		MedicationResponse medicationResponse = prescriptionService.addMedication(patient.getPatientId(),
				prescriptionId, medicationRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(medicationResponse);
	}

	@PutMapping("{prescriptionId}/medications/{medicationId}")
	public ResponseEntity<MedicationResponse> updateMedication(Authentication authentication,
			@PathVariable("prescriptionId") int prescriptionId, @PathVariable("medicationId") int medicationId,
			@RequestBody MedicationRequest medicationRequest) {
		Patient patient = userService.getPatientByUserName(authentication.getName());

		MedicationResponse response = prescriptionService.updateMedication(patient.getPatientId(), prescriptionId,
				medicationId, medicationRequest);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{prescriptionId}/medications/{medicationId}")
	public ResponseEntity<Void> deleteMedication(Authentication authentication,
			@PathVariable("prescriptionId") int prescriptionId, @PathVariable("medicationId") int medicationId) {
		Patient patient = userService.getPatientByUserName(authentication.getName());

		prescriptionService.deleteMedication(patient.getPatientId(), prescriptionId, medicationId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
