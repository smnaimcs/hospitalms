package com.patient.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patient.dto.PatientUpdateDTO;
import com.patient.model.Appointment;
import com.patient.model.Patient;
import com.patient.repository.AppointmentRepository;
import com.patient.repository.PatientRepository;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient updatePatient(Long id, PatientUpdateDTO updateDTO) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();

            if (updateDTO.getFirstName() != null) {
                patient.setFirstName(updateDTO.getFirstName());
            }

            if (updateDTO.getLastName() != null) {
                patient.setLastName(updateDTO.getLastName());
            }

            if (updateDTO.getEmail() != null) {
                patient.setEmail(updateDTO.getEmail());
            }

            if (updateDTO.getPhone() != null) {
                patient.setPhone(updateDTO.getPhone());
            }

            if (updateDTO.getAddress() != null) {
                patient.setAddress(updateDTO.getAddress());
            }

            return patientRepository.save(patient);
        }

        return null;
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> searchPatients(String query) {
        return patientRepository.findAll().stream()
                .filter(patient -> 
                    patient.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    patient.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                    patient.getEmail().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Patient> getPatientsByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(Appointment::getPatient)
                .distinct()
                .collect(Collectors.toList());
    }
}
