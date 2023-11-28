package com.doctor.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doctor.appointment.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    
}
