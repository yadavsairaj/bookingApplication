package com.doctor.appointment.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.appointment.entity.Doctor;

import com.doctor.appointment.service.DoctorService;

@RestController
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

     @GetMapping("/doctor/all")
   public ResponseEntity<List<Doctor>> getAllDoctors(){
    List<Doctor> doctors = doctorService.getAllDoctors();
    return ResponseEntity.ok(doctors);


   }
   @GetMapping("/doctor/{id}")
   public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id){
    Optional<Doctor> doctor = doctorService.getDoctorById(id);
    return doctor.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
   }

   @PostMapping("/doctor/create")
   public Doctor createDoctor(@RequestBody Doctor doctor){
    
    return doctorService.createDoctor(doctor);
   }

   @PutMapping("/doctor/update/{id}")
   public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor){
    Doctor updated = doctorService.updateDoctor(id, updatedDoctor);
    if(updated!=null){
        return ResponseEntity.ok(updated);
    }else{
        return ResponseEntity.notFound().build();
    }
   }

   @DeleteMapping("/doctor/delete/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id){
    doctorService.deleteDoctor(id);
    return ResponseEntity.noContent().build();
   }
}
