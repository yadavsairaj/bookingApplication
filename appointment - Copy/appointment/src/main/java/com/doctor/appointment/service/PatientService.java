package com.doctor.appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.doctor.appointment.entity.Appointment;
import com.doctor.appointment.entity.Doctor;
import com.doctor.appointment.entity.Patient;
import com.doctor.appointment.repository.AppointmentRepository;
import com.doctor.appointment.repository.DoctorRepository;
import com.doctor.appointment.repository.PatientRepository;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    
    public Patient createPatient(Patient patient){
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if(existingPatient.isPresent()){
            Patient patientToUpdate = existingPatient.get();
            patientToUpdate.setName(updatedPatient.getName());
            patientToUpdate.setAge(updatedPatient.getAge());
            patientToUpdate.setGender(updatedPatient.getGender());
            patientToUpdate.setIssue(updatedPatient.getIssue());
            patientToUpdate.setEmail(updatedPatient.getEmail());
            return patientRepository.save(patientToUpdate);
        }else{return null;}
        
    }

    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }

    public static Optional<Appointment> findByid(Long id) {
        return null;
    }

    
}
