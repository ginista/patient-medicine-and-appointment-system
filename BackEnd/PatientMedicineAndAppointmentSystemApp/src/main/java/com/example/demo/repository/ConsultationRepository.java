package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation,Integer>{
	@Query("SELECT c FROM Consultation c WHERE c.slotDate = :selectedDate and c.availableTokens > 0 ")
	List<Consultation> findAvailableSlotsByDate(@Param("selectedDate") LocalDate selectedDate);

}

