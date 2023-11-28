package com.doctor.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doctor.appointment.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
}
