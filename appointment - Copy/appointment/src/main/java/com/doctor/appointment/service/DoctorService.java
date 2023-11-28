package com.doctor.appointment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doctor.appointment.entity.Doctor;

import com.doctor.appointment.repository.DoctorRepository;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(id);
        if(existingDoctor.isPresent()){
            Doctor doctorToUpdate = existingDoctor.get();
            doctorToUpdate.setName(updatedDoctor.getName());
            doctorToUpdate.setSpecialization(updatedDoctor.getSpecialization());
            return doctorRepository.save(doctorToUpdate);
        }else{return null;}
        
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

  

}
