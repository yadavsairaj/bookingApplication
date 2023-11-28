package com.doctor.appointment.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.doctor.appointment.entity.Appointment;
import com.doctor.appointment.entity.Doctor;
import com.doctor.appointment.entity.Patient;
import com.doctor.appointment.repository.AppointmentRepository;
import com.doctor.appointment.repository.DoctorRepository;
import com.doctor.appointment.repository.PatientRepository;

@Service
public class AppointmentService {

     @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    private final JavaMailSender javaMailSender;

    @Autowired
    public AppointmentService(
        DoctorRepository doctorRepository,
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        JavaMailSender javaMailSender
    ){
        this.doctorRepository=doctorRepository;
        this.patientRepository=patientRepository;
        this.appointmentRepository=appointmentRepository;
        this.javaMailSender=javaMailSender;
    }

    // public List<Appointment> getAllAppointments() {
    //     return appointmentRepository.findAll();
    // }

    

    // public Optional<Appointment> getappointmentById(Long id) {
    //     return appointmentRepository.findById(id);
    // }

    // public Appointment createAppointment(Appointment appointment) {
    //     return appointmentRepository.save(appointment);
    // }

    // public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
    //     Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
    //     if(existingAppointment.isPresent()){
    //         Appointment appointmentToUpdate = existingAppointment.get();
    //         appointmentToUpdate.setAppointmentDateTime(updatedAppointment.getAppointmentDateTime());
            
    //         return appointmentRepository.save(appointmentToUpdate);
    //     }else{return null;}
    // }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public List<String> getAvailableSlots(Long id, LocalDateTime appointmentDateTime){
        
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);

        if(optionalDoctor.isPresent()){
            Doctor doctor = optionalDoctor.get();
            List<String> doctorSchedule = getDoctorSchedule(doctor);
            List<String> bookedSlots = getBookedSlots(id, appointmentDateTime);
            List<String> availableSlots = new ArrayList<>(doctorSchedule);
            availableSlots.removeAll(bookedSlots);

            //logging
            // System.out.println("Doctors Schedule: " + doctorSchedule);
            // System.out.println("bookedSlots : " + bookedSlots);
            // System.out.println("availableSlots : " + availableSlots);


            return availableSlots;
        }else {
            //doctor not found
            return Collections.emptyList();
        }
        //return List.of("9.00 AM", "10.00 AM", "11.00 AM", "12.00 AM", "1.00 PM");
    }

    

    

    public String bookAppointment(Long id, LocalDateTime appointmentDateTime, Patient patient){
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if(optionalDoctor.isPresent()){
            //System.out.println("******************THE DOCTOR IS PRESENT*************************");
            Doctor doctor = optionalDoctor.get();
                
            //check if provided appointment date and time is withing doctors working hours
            List<String> availableSlots = getAvailableSlots(id, appointmentDateTime);
            String formattedDateTime = appointmentDateTime.toString();
            

            if(availableSlots.contains(formattedDateTime)){
                //System.out.println("******************THE available slot contains formatted time*************************");
            //slot available, proceed with booking{not entering this if method}
            patientRepository.save(patient);

            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setAppointmentDateTime(appointmentDateTime);
            appointment.setNotes("BOOKED");
            appointmentRepository.save(appointment);
            
            removeBookedSlot(id, appointmentDateTime);

            sendConfirmationEmail(patient, doctor, formattedDateTime);

             return "Appointment booked successfully";
        }else{
            return "Slot not available. Please choose another slot.";
        }
        }else{
            return "Doctor not found. ";
        }

        

    }

    private List<String> getDoctorSchedule(Doctor doctor){
        LocalTime startTime = LocalTime.of(9,0);
        LocalTime endTime = LocalTime.of(17,0);
        List<String> doctorSchedule = new ArrayList<>();

        while(startTime.isBefore(endTime)){
            doctorSchedule.add(startTime.toString());
            startTime = startTime.plusHours(1);
        }
        return doctorSchedule;
    }

    private List<String> getBookedSlots(Long id, LocalDateTime appointmentDateTime){
        //querying the databse to get booked slots for sepecified doctor , time and date
        List<Appointment> bookedAppointments = appointmentRepository.findByIdAndAppointmentDateTime(id, appointmentDateTime);
        //convert booked appointmn to a list of slot strings
        return bookedAppointments.stream().map(appointment -> appointment.getAppointmentDateTime().toLocalTime().toString()).collect(Collectors.toList());
    }

    private void removeBookedSlot(Long id, LocalDateTime appointmentDateTime) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            List<String> availableSlots = getAvailableSlots(id, appointmentDateTime);
            String formattedDateTime = appointmentDateTime.toString();

            if (availableSlots.contains(formattedDateTime)) {
                availableSlots.remove(formattedDateTime);
                // Assuming availableSlots are stored in the Doctor entity
                //doctor.setAvailableSlots(availableSlots);
                doctorRepository.save(doctor);
            }
        }
    }


    private void sendConfirmationEmail(Patient patient, Doctor doctor, String formattedDateTime){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(patient.getEmail());
        message.setSubject("Appointment confirmation");
        message.setText("Dear "+ patient.getName() +",\n\n Your appointment with " + doctor.getName()+ " on" + formattedDateTime +" has been booked successfully.\n\n Regards");
        javaMailSender.send(message);
    }

    // public void deleteAppointment(Long id) {
    //     appointmentRepository.deleteById(id);
    // }

    // public void bookAppointment(long id, long id, LocalDateTime appointmenDateTime){
    //     Patient Cpatient = patientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient not found with id: "+ id));
    //     Doctor Cdoctor = doctorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Doctor not found with id: "+ id));

    //     Appointment appointment = new Appointment();
    //     appointment.setPatient(Cpatient);
    //     appointment.setDoctor(Cdoctor);
    //     appointment.setAppointmentDateTime(appointmenDateTime);

    //     appointmentRepository.save(appointment);

    //     sendConfirmationEmail(Cpatient.getEmail(), "Appointment booked succesfullly");
    // }
    // private void sendConfirmationEmail(String email, String message){
    //     System.out.println("Email sent to"+ email + "with message" + message);
    // }

}
