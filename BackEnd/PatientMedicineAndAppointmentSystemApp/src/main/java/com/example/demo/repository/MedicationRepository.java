package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

	@Query("From Medication m where m.prescription.patient.patientId = :patientId and m.prescription.id = :prescriptionId and m.id = :medicationId")
	Optional<Medication> findByPatientIdAndPrescriptionIdAndMedicationId(int patientId, int prescriptionId,
			int medicationId);

	@Modifying
	@Query("Delete From Medication m where m.prescription.patient.patientId = :patientId and m.prescription.id = :prescriptionId and m.id = :medicationId")
	void deleteByPatientIdAndPrescriptionIdAndMedicationId(int patientId, int prescriptionId, int medicationId);

	@Query("select count(m) > 0 From Medication m where m.prescription.patient.patientId = :patientId and m.prescription.id = :prescriptionId and m.id <> :medicationId")
	boolean hasMoreMedications(int patientId, int prescriptionId, int medicationId);

}
