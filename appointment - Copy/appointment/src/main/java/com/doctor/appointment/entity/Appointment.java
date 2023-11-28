package com.doctor.appointment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable = false)
    private Doctor doctor;

    
    @ManyToOne
    @JoinColumn(name="patient_id", nullable = false)
    private Patient patient;
    private String slot;
   
    private LocalDateTime appointmentDateTime;
    private String notes;

     

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
     public String getSlot() {
        return slot;
    }
    public void setSlot(String slot) {
        this.slot = slot;
    }
    
    public Appointment(){}
    public Appointment(Long id, LocalDateTime appointmentDateTime, String notes, Doctor doctor, Patient patient, String slot) {
        this.id = id;
        this.appointmentDateTime = appointmentDateTime;
        this.notes = notes;
        this.doctor = doctor;
        this.patient = patient;
        this.slot=slot;
    }
    @Override
    public String toString() {
        return "Appointment [id=" + id + ", appointmentDateTime=" + appointmentDateTime + ", notes=" + notes  
                + ", doctor=" + doctor + ", patient=" + patient + ", slot=" + slot + "]";
    }
    
    

    
}
