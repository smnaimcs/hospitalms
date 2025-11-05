package com.patient.service;

import com.patient.model.Doctor;
import com.patient.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor1;
    private Doctor doctor2;

    @BeforeEach
    void setUp() {
        doctor1 = Doctor.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("Johnson")
                .specialization("Cardiology")
                .qualifications("MD, FACC")
                .experienceYears(15)
                .email("m.johnson@hospital.com")
                .phone("123-456-7801")
                .build();

        doctor2 = Doctor.builder()
                .id(2L)
                .firstName("Sarah")
                .lastName("Williams")
                .specialization("Dermatology")
                .qualifications("MD, FAAD")
                .experienceYears(10)
                .email("s.williams@hospital.com")
                .phone("123-456-7802")
                .build();
    }

    @Test
    void getAllDoctors_ShouldReturnAllDoctors() {
        // Arrange
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);
        when(doctorRepository.findAll()).thenReturn(doctors);

        // Act
        List<Doctor> result = doctorService.getAllDoctors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialization());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorById_WithValidId_ShouldReturnDoctor() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));

        // Act
        Optional<Doctor> result = doctorService.getDoctorById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Michael", result.get().getFirstName());
        assertEquals("Cardiology", result.get().getSpecialization());
    }

    @Test
    void getDoctorsBySpecialization_ShouldReturnFilteredDoctors() {
        // Arrange
        List<Doctor> cardiologists = Arrays.asList(doctor1);
        when(doctorRepository.findBySpecializationContainingIgnoreCase("Cardiology"))
                .thenReturn(cardiologists);

        // Act
        List<Doctor> result = doctorService.getDoctorsBySpecialization("Cardiology");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialization());
        verify(doctorRepository, times(1)).findBySpecializationContainingIgnoreCase("Cardiology");
    }

    @Test
    void smartSearch_ShouldReturnMatchingDoctors() {
        // Arrange
        List<Doctor> searchResults = Arrays.asList(doctor1);
        when(doctorRepository.smartSearch("cardio")).thenReturn(searchResults);

        // Act
        List<Doctor> result = doctorService.smartSearch("cardio");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialization());
        verify(doctorRepository, times(1)).smartSearch("cardio");
    }
}