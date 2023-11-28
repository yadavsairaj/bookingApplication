package com.doctor.appointment.repository;

import java.time.LocalDateTime;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doctor.appointment.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findByIdAndAppointmentDateTime(Long id, LocalDateTime appointmentDateTime);
}
