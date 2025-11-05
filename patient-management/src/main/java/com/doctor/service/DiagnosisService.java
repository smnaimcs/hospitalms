package com.doctor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doctor.model.Diagnosis;
import com.doctor.repository.DiagnosisRepository;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.patient.service.DoctorService;
import com.patient.service.PatientService;

@Service
public class DiagnosisService {
    
    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    public List<Diagnosis> getDiagnosesByPatientId(Long patientId) {
        return diagnosisRepository.findByPatientId(patientId);
    }

    public List<Diagnosis> getDiagnosesByDoctorId(Long doctorId) {
        return diagnosisRepository.findByDoctorId(doctorId);
    }

    public List<Diagnosis> getDiagNosesByDoctorAndPatient(Long doctorId, Long patientId) {
        return diagnosisRepository.findByDoctorAndPatient(doctorId, patientId);
    }

    public Diagnosis addDiagnosis(Long doctorId, Long patientId, Diagnosis diagnosis) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        Optional<Patient> patient = patientService.getPatientById(patientId);

        if (doctor.isPresent() && patient.isPresent()) {
            diagnosis.setDoctor(doctor.get());
            diagnosis.setPatient(patient.get());
            diagnosis.setDiagnosedDate(LocalDateTime.now());
            return diagnosisRepository.save(diagnosis);
        }

        return null;
    }
}
