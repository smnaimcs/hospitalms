package com.patient.service;

import com.patient.dto.AppointmentRequestDTO;
import com.patient.model.Appointment;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.patient.repository.AppointmentRepository;
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
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;
    private AppointmentRequestDTO appointmentRequestDTO;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("Johnson")
                .specialization("Cardiology")
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .appointmentDateTime(LocalDateTime.now().plusDays(1))
                .status("SCHEDULED")
                .patient(patient)
                .doctor(doctor)
                .build();

        appointmentRequestDTO = new AppointmentRequestDTO();
        appointmentRequestDTO.setPatientId(1L);
        appointmentRequestDTO.setDoctorId(1L);
        appointmentRequestDTO.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    void createAppointment_WithValidData_ShouldCreateAppointment() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment result = appointmentService.createAppointment(appointmentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
        assertEquals(patient, result.getPatient());
        assertEquals(doctor, result.getDoctor());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void createAppointment_WithInvalidPatient_ShouldReturnNull() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(Optional.empty());

        // Act
        Appointment result = appointmentService.createAppointment(appointmentRequestDTO);

        // Assert
        assertNull(result);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void getAppointmentsByPatientId_ShouldReturnAppointments() {
        // Arrange
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);

        // Act
        List<Appointment> result = appointmentService.getAppointmentsByPatientId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getPatient().getId());
        verify(appointmentRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void cancelAppointment_WithValidId_ShouldCancelAppointment() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        boolean result = appointmentService.cancelAppointment(1L);

        // Assert
        assertTrue(result);
        assertEquals("CANCELLED", appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void cancelAppointment_WithInvalidId_ShouldReturnFalse() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        boolean result = appointmentService.cancelAppointment(999L);

        // Assert
        assertFalse(result);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}