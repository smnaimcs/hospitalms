package com.patient.service;

import com.patient.dto.PatientUpdateDTO;
import com.patient.model.Patient;
import com.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        patient1 = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .address("123 Main St")
                .build();

        patient2 = Patient.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@email.com")
                .phone("123-456-7891")
                .dateOfBirth(LocalDate.of(1990, 8, 22))
                .address("456 Oak Ave")
                .build();
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(patient1, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_WithValidId_ShouldReturnPatient() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));

        // Act
        Optional<Patient> result = patientService.getPatientById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
    }

    @Test
    void getPatientById_WithInvalidId_ShouldReturnEmpty() {
        // Arrange
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Patient> result = patientService.getPatientById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updatePatient_WithValidData_ShouldUpdatePatient() {
        // Arrange
        PatientUpdateDTO updateDTO = new PatientUpdateDTO();
        updateDTO.setFirstName("Johnny");
        updateDTO.setPhone("555-1234");
        updateDTO.setAddress("789 New Street");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient1);

        // Act
        Patient result = patientService.updatePatient(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
        assertEquals("555-1234", result.getPhone());
        assertEquals("789 New Street", result.getAddress());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WithInvalidId_ShouldReturnNull() {
        // Arrange
        PatientUpdateDTO updateDTO = new PatientUpdateDTO();
        updateDTO.setFirstName("Johnny");
        
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Patient result = patientService.updatePatient(999L, updateDTO);

        // Assert
        assertNull(result);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void createPatient_ShouldSaveAndReturnPatient() {
        // Arrange
        when(patientRepository.save(any(Patient.class))).thenReturn(patient1);

        // Act
        Patient result = patientService.createPatient(patient1);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(patientRepository, times(1)).save(patient1);
    }
}