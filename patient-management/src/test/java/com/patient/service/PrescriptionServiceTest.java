package com.patient.service;

import com.doctor.model.Prescription;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.doctor.repository.PrescriptionRepository;
import com.doctor.repository.PrescriptionService;

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
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Patient patient;
    private Doctor doctor;
    private Prescription prescription;

    @BeforeEach
    void setUp() {
        patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        doctor = Doctor.builder().id(1L).firstName("Michael").lastName("Johnson").build();
        
        prescription = Prescription.builder()
                .id(1L)
                .medicineName("Lisinopril")
                .doses("10mg")
                .frequency("Once daily")
                .durationDays(30)
                .instructions("Take in the morning")
                .prescribedDate(LocalDateTime.now())
                .patient(patient)
                .doctor(doctor)
                .build();
    }

    @Test
    void prescribeMedicine_WithValidData_ShouldSavePrescription() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(doctor));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        Prescription result = prescriptionService.prescribeMedicine(1L, 1L, prescription);

        // Assert
        assertNotNull(result);
        assertEquals("Lisinopril", result.getMedicineName());
        assertEquals("10mg", result.getDoses());
        assertEquals(patient, result.getPatient());
        assertEquals(doctor, result.getDoctor());
        assertNotNull(result.getPrescribedDate());
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void getPrescriptionsByPatientId_ShouldReturnPrescriptions() {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionRepository.findByPatientId(1L)).thenReturn(prescriptions);

        // Act
        List<Prescription> result = prescriptionService.getPrescriptionsByPatientId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lisinopril", result.get(0).getMedicineName());
        verify(prescriptionRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void getPrescriptionsByDoctorAndPatient_ShouldReturnPrescriptions() {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionRepository.findByDoctorAndPatient(1L, 1L)).thenReturn(prescriptions);

        // Act
        List<Prescription> result = prescriptionService.getPrescriptionsByDoctorAndPatient(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getDoctor().getId());
        assertEquals(1L, result.get(0).getPatient().getId());
        verify(prescriptionRepository, times(1)).findByDoctorAndPatient(1L, 1L);
    }
}