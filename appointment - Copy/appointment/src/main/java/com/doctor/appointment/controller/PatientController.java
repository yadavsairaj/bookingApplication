package com.doctor.appointment.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.doctor.appointment.entity.Appointment;
import com.doctor.appointment.entity.Doctor;
import com.doctor.appointment.entity.Patient;
import com.doctor.appointment.repository.AppointmentRepository;
import com.doctor.appointment.repository.DoctorRepository;
import com.doctor.appointment.repository.PatientRepository;
import com.doctor.appointment.service.AppointmentService;
import com.doctor.appointment.service.DoctorService;
import com.doctor.appointment.service.PatientService;

@RestController
//@RequestMapping("/api/patients")
public class PatientController {
    
   @Autowired
    private final AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

   
    @Autowired
    public PatientController(
        AppointmentService appointmentService
    ){
        this.appointmentService=appointmentService;
    }
   

   @GetMapping("/patient/all")
   public ResponseEntity<List<Patient>> getAllPatients(){
    List<Patient> patients = patientService.getAllPatients();
    return ResponseEntity.ok(patients);


   }
   @GetMapping("/patient/{id}")
   public ResponseEntity<Patient> getPatientById(@PathVariable Long id){
    Optional<Patient> patient = patientService.getPatientById(id);
    return patient.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
   }

//    @PostMapping("/patient/create")
//    public Patient createPatient(@RequestBody Patient patient){
    
//     return patientService.createPatient(patient);
//    }

//    @PutMapping("/patient/update/{id}")
//    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient){
//     Patient updated = patientService.updatePatient(id, updatedPatient);
//     if(updated!=null){
//         return ResponseEntity.ok(updated);
//     }else{
//         return ResponseEntity.notFound().build();
//     }
//    }

   @DeleteMapping("/patient/delete/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
    patientService.deletePatient(id);
    return ResponseEntity.noContent().build();
   }
   
   
   @PostMapping("/patient/book-appointment")
   public ResponseEntity<String> bookAppointment(
    @RequestParam Long id,
    @RequestParam int year,
    @RequestParam int month,
    @RequestParam int day,
    @RequestParam String slot,
    @RequestBody Patient patient
   ){

            //logging
            //System.out.println("Received parameters: id: " + id + ", year: " + year + ", month: " + month + ", day: " + day + " slot: " + slot);        
    
            LocalDateTime appointmentDateTime = LocalDateTime.of(year, month, day, 0, 0).plusHours(Integer.parseInt(slot.split(":")[0])).plusMinutes(Integer.parseInt(slot.split(":")[1]));
            String result = appointmentService.bookAppointment(id, appointmentDateTime, patient);
            return ResponseEntity.ok(result);
    }

     @GetMapping("/doctors/available-slots")
    public ResponseEntity<List<String>> getAvailableSlots(
        @RequestParam Long id,
        @RequestParam int year,
        @RequestParam int month,
        @RequestParam int day){
        LocalDateTime appointmentDateTime = LocalDateTime.of(year, month, day, 0, 0);
        List<String> availableSlots = appointmentService.getAvailableSlots(id, appointmentDateTime);
        return ResponseEntity.ok(availableSlots);
    }
   }
   

