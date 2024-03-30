package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Medication;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exception.AppointmentNotFoundException;
import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.exception.PrescriptionNotFoundException;
import com.example.demo.model.CreatePrescriptionRequest;
import com.example.demo.model.MedicationRequest;
import com.example.demo.model.MedicationResponse;
import com.example.demo.model.PrescriptionResponse;
import com.example.demo.model.enums.AppointmentStatus;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.MedicationRepository;
import com.example.demo.repository.PrescriptionRepository;

import jakarta.transaction.Transactional;

@Service
public class PrescriptionService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private MedicationRepository medicationRepository;

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	public PrescriptionResponse createPrescriptionWithMedications(Patient patient,
			CreatePrescriptionRequest createPrescriptionRequest) {
		Appointment appointment = appointmentRepository.findById(createPrescriptionRequest.getAppointmentId())
				.orElseThrow(AppointmentNotFoundException::new);

		Doctor doctor = doctorRepository.findById(createPrescriptionRequest.getDoctorId())
				.orElseThrow(DoctorNotFoundException::new);

		List<Medication> medications = createPrescriptionRequest.getMedicationRequests().stream()
				.map(this::mapMedicationRequestToMedication).collect(Collectors.toList());

		Prescription prescription = new Prescription();
		prescription.setDoctor(doctor);
		prescription.setPatient(patient);
		prescription.setPrescriptionDate(LocalDate.now());
		prescription.setMedications(medications);

		medications.forEach(medication -> medication.setPrescription(prescription));

		Prescription savedPrescription = prescriptionRepository.save(prescription);

		updateAppointmentCompletion(appointment, savedPrescription);

		return mapPrescriptionToPrescriptionResponse(savedPrescription);
	}

	private void updateAppointmentCompletion(Appointment appointment, Prescription prescription) {
		appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
		appointment.setPrescription(prescription);

		appointmentRepository.save(appointment);
	}

	public List<PrescriptionResponse> getPrescriptions(int patientId) {
		List<Prescription> prescriptions = prescriptionRepository.findAllByPatientId(patientId);

		return prescriptions.stream().map(this::mapPrescriptionToPrescriptionResponse).collect(Collectors.toList());
	}

	public MedicationResponse addMedication(int patientId, int prescriptionId, MedicationRequest medicationRequest) {

		Prescription prescription = prescriptionRepository.findByPatientIdAndPrescriptionId(patientId, prescriptionId)
				.orElseThrow(PrescriptionNotFoundException::new);

		Medication medication = mapMedicationRequestToMedication(medicationRequest);
		medication.setPrescription(prescription);

		Medication savedMedication = medicationRepository.save(medication);

		return mapMedicationToMedicationResponse(savedMedication);
	}

	public MedicationResponse updateMedication(int patientId, int prescriptionId, int medicationId,
			MedicationRequest medicationRequest) {
		Medication medication = medicationRepository
				.findByPatientIdAndPrescriptionIdAndMedicationId(patientId, prescriptionId, medicationId)
				.map(md -> mapMedication(medicationRequest, md))
				.orElseThrow(() -> new IllegalArgumentException("Medication not found with ID: " + medicationId));

		Medication updatedMedication = medicationRepository.save(medication);

		return mapMedicationToMedicationResponse(updatedMedication);
	}

	private Medication mapMedicationRequestToMedication(MedicationRequest medicationRequest) {
		return mapMedication(medicationRequest, new Medication());
	}

	private Medication mapMedication(MedicationRequest medicationRequest, Medication medication) {
		medication.setDosage(medicationRequest.getDosage());
		medication.setFrequency(medicationRequest.getFrequency());
		medication.setInstructions(medicationRequest.getInstructions());
		medication.setMedicationName(medicationRequest.getMedicationName());
		medication.setNoOfDays(medicationRequest.getNoOfDays());
		return medication;
	}

	private PrescriptionResponse mapPrescriptionToPrescriptionResponse(Prescription prescription) {

		PrescriptionResponse prescriptionResponse = new PrescriptionResponse();

		prescriptionResponse.setPrescriptionId(prescription.getId());
		prescriptionResponse.setDoctorName(prescription.getDoctor().getDoctorName());
		prescriptionResponse.setPatientName(prescription.getPatient().getName());
		prescriptionResponse.setPrescriptionDate(prescription.getPrescriptionDate());
		List<MedicationResponse> medicationResponse = prescription.getMedications().stream().map(this::mapMedicationToMedicationResponse)
				.collect(Collectors.toList());
		prescriptionResponse.setMedications(medicationResponse);

		return prescriptionResponse;
	}

	private MedicationResponse mapMedicationToMedicationResponse(Medication medication) {

		MedicationResponse medicationResponse = new MedicationResponse();

		medicationResponse.setMedicationId(medication.getId());
		medicationResponse.setDosage(medication.getDosage());
		medicationResponse.setFrequency(medication.getFrequency());
		medicationResponse.setInstructions(medication.getInstructions());
		medicationResponse.setMedicationName(medication.getMedicationName());
		medicationResponse.setNoOfDays(medication.getNoOfDays());

		return medicationResponse;

	}

	@Transactional
	public void deleteMedication(int patientId, int prescriptionId, int medicationId) {
		boolean hasMoreMedications = medicationRepository.hasMoreMedications(patientId, prescriptionId, medicationId);
		if (hasMoreMedications) {
			medicationRepository.deleteByPatientIdAndPrescriptionIdAndMedicationId(patientId, prescriptionId,
					medicationId);
		} else {
			throw new RuntimeException(
					"Delete medication failed. Atleast one medication is required for a prescription");
		}
	}

}
