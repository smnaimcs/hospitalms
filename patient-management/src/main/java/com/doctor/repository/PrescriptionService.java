package com.doctor.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doctor.model.Prescription;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.patient.service.DoctorService;
import com.patient.service.PatientService;

@Service
public class PrescriptionService {
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    public List<Prescription> getPrescriptionsByDoctorAndPatient(Long doctorId, Long patientId) {
        return prescriptionRepository.findByDoctorAndPatient(doctorId, patientId);
    }

    public Prescription prescribeMedicine(Long doctorId, Long patientId, Prescription prescription) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        Optional<Patient> patient = patientService.getPatientById(patientId);

        if (doctor.isPresent() && patient.isPresent()) {
            prescription.setDoctor(doctor.get());
            prescription.setPatient(patient.get());
            prescription.setPrescribedDate(LocalDateTime.now());
            return prescriptionRepository.save(prescription);
        }

        return null;
    }
}
