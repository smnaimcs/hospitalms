package com.patient.config;

import com.patient.model.*;
import com.patient.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample patients
        Patient patient1 = new Patient("John", "Doe", "john.doe@email.com", "123-456-7890", 
                                     LocalDate.of(1985, 5, 15), "123 Main St, City, State");
        Patient patient2 = new Patient("Jane", "Smith", "jane.smith@email.com", "123-456-7891", 
                                     LocalDate.of(1990, 8, 22), "456 Oak Ave, City, State");
        
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        
        // Create sample doctors
        Doctor doctor1 = new Doctor("Michael", "Johnson", "Cardiology", "MD, FACC", 15, 
                                  "m.johnson@hospital.com", "123-456-7801");
        Doctor doctor2 = new Doctor("Sarah", "Williams", "Dermatology", "MD, FAAD", 10, 
                                  "s.williams@hospital.com", "123-456-7802");
        Doctor doctor3 = new Doctor("Robert", "Brown", "Neurology", "MD, PhD", 20, 
                                  "r.brown@hospital.com", "123-456-7803");
        
        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);
        doctorRepository.save(doctor3);
        
        // Create sample appointments
        Appointment appointment1 = new Appointment(LocalDateTime.now().plusDays(1), "SCHEDULED", patient1, doctor1);
        Appointment appointment2 = new Appointment(LocalDateTime.now().plusDays(3), "SCHEDULED", patient2, doctor2);
        
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        
        // Create sample medical records
        MedicalRecord record1 = new MedicalRecord(LocalDate.now().minusMonths(2), "Hypertension", 
                                                "Medication and lifestyle changes", "Lisinopril 10mg", 
                                                "Patient responding well to treatment", patient1, doctor1);
        MedicalRecord record2 = new MedicalRecord(LocalDate.now().minusMonths(1), "Skin rash", 
                                                "Topical treatment", "Hydrocortisone cream", 
                                                "Rash cleared after treatment", patient2, doctor2);
        
        medicalRecordRepository.save(record1);
        medicalRecordRepository.save(record2);
    }
}