package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Consultation;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.model.AppointmentRequest;
import com.example.demo.model.AppointmentResponse;
import com.example.demo.model.ConsultationResponse;
import com.example.demo.model.enums.AppointmentStatus;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.ConsultationRepository;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private ConsultationRepository consultationRepository;

	public List<ConsultationResponse> findAvailableSlotsByDate(LocalDate selectedDate) {
		List<Consultation> consultation = consultationRepository.findAvailableSlotsByDate(selectedDate);
		return consultation.stream().map(this::mapConsultationResponse).collect(Collectors.toList());
	}

	private ConsultationResponse mapConsultationResponse(Consultation consultation) {
		ConsultationResponse consultationResponse = new ConsultationResponse();

		consultationResponse.setAvailableTokens(consultation.getAvailableTokens());
		consultationResponse.setConsultationCharge(consultation.getConsultationCharge());
		consultationResponse.setConsultationId(consultation.getId());
		consultationResponse.setDoctorName(consultation.getDoctor().getDoctorName());
		consultationResponse.setDoctorSpecialization(consultation.getDoctor().getSpecialist());
		consultationResponse.setFromTime(consultation.getFromTime());
		consultationResponse.setSlotDate(consultation.getSlotDate());
		consultationResponse.setTotalTokens(consultation.getTotalTokens());
		consultationResponse.setToTime(consultation.getToTime());
		consultationResponse.setLastBookedTokenNumber(consultation.getLastBookedTokenNumber());

		return consultationResponse;
	}

	public AppointmentResponse bookAppointment(Patient patient, AppointmentRequest appointmentRequest) {
		Consultation consultation = consultationRepository.findById(appointmentRequest.getConsultationId())
				.orElseThrow(() -> new RuntimeException("Consultation not found"));

		Appointment appointment = new Appointment();
		appointment.setAppointmentBookedDate(LocalDate.now());
		appointment.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointment.setPatient(patient);
		appointment.setProblem(appointmentRequest.getProblem());

		int updatedAvailableTokens = consultation.getAvailableTokens() - 1;
		consultation.setAvailableTokens(updatedAvailableTokens);

		int tokenNumber = consultation.getLastBookedTokenNumber() + 1;
		consultation.setLastBookedTokenNumber(tokenNumber);

		appointment.setConsultation(consultation);
		appointment.setTokenNumber(tokenNumber);

		Appointment createdAppointment = appointmentRepository.save(appointment);

		return mapAppointmentResponse(createdAppointment);
	}

	private AppointmentResponse mapAppointmentResponse(Appointment appointment) {

		AppointmentResponse appointmentResponse = new AppointmentResponse();
		appointmentResponse.setAppointmentId(appointment.getAppointmentId());
		appointmentResponse.setTokenNumber(appointment.getConsultation().getLastBookedTokenNumber());
		appointmentResponse.setPatientName(appointment.getPatient().getName());
		appointmentResponse.setConsultationId(appointment.getConsultation().getId());
		appointmentResponse.setSlotDate(appointment.getConsultation().getSlotDate());
		appointmentResponse.setFromTime(appointment.getConsultation().getFromTime());
		appointmentResponse.setToTime(appointment.getConsultation().getToTime());
		appointmentResponse.setConsultationCharge(appointment.getConsultation().getConsultationCharge());
		appointmentResponse.setDoctorName(appointment.getConsultation().getDoctor().getDoctorName());
		appointmentResponse.setProblem(appointment.getProblem());
		appointmentResponse.setAppointmentBookedDate(appointment.getAppointmentBookedDate());
		appointmentResponse.setAppointmentStatus(appointment.getAppointmentStatus());
		
		Optional.ofNullable(appointment.getPrescription()).map(Prescription::getId)
				.ifPresent(appointmentResponse::setPrescriptionId);

		return appointmentResponse;
	}

	// View Appointments
	public List<AppointmentResponse> findByPatientId(int patientId) {
		List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
		return appointments.stream().map(this::mapAppointmentResponse).collect(Collectors.toList());
	}
}
