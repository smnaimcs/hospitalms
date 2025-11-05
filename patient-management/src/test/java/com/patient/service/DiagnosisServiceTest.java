package com.patient.service;

import com.doctor.model.Diagnosis;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.doctor.repository.DiagnosisRepository;
import com.doctor.service.DiagnosisService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosisServiceTest {

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DiagnosisService diagnosisService;

    private Patient patient;
    private Doctor doctor;
    private Diagnosis diagnosis;

    @BeforeEach
    void setUp() {
        patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        doctor = Doctor.builder().id(1L).firstName("Michael").lastName("Johnson").build();
        
        diagnosis = Diagnosis.builder()
                .id(1L)
                .diagnosisCode("I10")
                .diagnosisName("Essential hypertension")
                .description("Primary high blood pressure")
                .diagnosedDate(LocalDateTime.now())
                .patient(patient)
                .doctor(doctor)
                .build();
    }

    @Test
    void addDiagnosis_WithValidData_ShouldSaveDiagnosis() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(doctor));
        when(diagnosisRepository.save(any(Diagnosis.class))).thenReturn(diagnosis);

        // Act
        Diagnosis result = diagnosisService.addDiagnosis(1L, 1L, diagnosis);

        // Assert
        assertNotNull(result);
        assertEquals("I10", result.getDiagnosisCode());
        assertEquals(patient, result.getPatient());
        assertEquals(doctor, result.getDoctor());
        assertNotNull(result.getDiagnosedDate());
        verify(diagnosisRepository, times(1)).save(any(Diagnosis.class));
    }

    @Test
    void getDiagnosesByPatientId_ShouldReturnDiagnoses() {
        // Arrange
        List<Diagnosis> diagnoses = Arrays.asList(diagnosis);
        when(diagnosisRepository.findByPatientId(1L)).thenReturn(diagnoses);

        // Act
        List<Diagnosis> result = diagnosisService.getDiagnosesByPatientId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("I10", result.get(0).getDiagnosisCode());
        verify(diagnosisRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void getDiagnosesByDoctorAndPatient_ShouldReturnDiagnoses() {
        // Arrange
        List<Diagnosis> diagnoses = Arrays.asList(diagnosis);
        when(diagnosisRepository.findByDoctorAndPatient(1L, 1L)).thenReturn(diagnoses);

        // Act
        List<Diagnosis> result = diagnosisService.getDiagNosesByDoctorAndPatient(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getDoctor().getId());
        assertEquals(1L, result.get(0).getPatient().getId());
        verify(diagnosisRepository, times(1)).findByDoctorAndPatient(1L, 1L);
    }
}