package com.patient.service;

import com.admin.dto.AdminDashboardStats;
import com.admin.model.Admin;
import com.admin.repository.AdminRepository;
import com.admin.service.AdminService;
import com.patient.model.*;
import com.patient.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;
    private Admin admin;

    @BeforeEach
    void setUp() {
        // Setup test data
        patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .address("123 Main St")
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("Johnson")
                .specialization("Cardiology")
                .qualifications("MD, FACC")
                .experienceYears(15)
                .email("m.johnson@hospital.com")
                .phone("123-456-7801")
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .appointmentDateTime(LocalDateTime.now().plusDays(1))
                .status("REQUESTED")
                .patient(patient)
                .doctor(doctor)
                .build();

        admin = Admin.builder()
                .id(1L)
                .username("admin")
                .password("admin123")
                .email("admin@hospital.com")
                .firstName("System")
                .lastName("Administrator")
                .active(true)
                .build();
    }

    // Appointment Management Tests
    @Test
    void getAllAppointments_ShouldReturnAllAppointments() {
        // Arrange
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // Act
        List<Appointment> result = adminService.getAllAppointments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("REQUESTED", result.get(0).getStatus());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void getPendingAppointments_ShouldReturnOnlyRequestedAppointments() {
        // Arrange
        Appointment approvedAppointment = Appointment.builder()
                .id(2L)
                .status("APPROVED")
                .patient(patient)
                .doctor(doctor)
                .build();

        List<Appointment> allAppointments = Arrays.asList(appointment, approvedAppointment);
        when(appointmentRepository.findAll()).thenReturn(allAppointments);

        // Act
        List<Appointment> result = adminService.getPendingAppointments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("REQUESTED", result.get(0).getStatus());
    }

    @Test
    void approveAppointment_WithValidId_ShouldUpdateStatus() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment result = adminService.approveAppointment(1L);

        // Assert
        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void approveAppointment_WithInvalidId_ShouldReturnNull() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Appointment result = adminService.approveAppointment(999L);

        // Assert
        assertNull(result);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void rejectAppointment_WithValidId_ShouldUpdateStatus() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment result = adminService.rejectAppointment(1L);

        // Assert
        assertNotNull(result);
        assertEquals("REJECTED", result.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void scheduleAppointment_WithValidData_ShouldUpdateDateTimeAndStatus() {
        // Arrange
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(2);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment result = adminService.scheduleAppointment(1L, newDateTime);

        // Assert
        assertNotNull(result);
        assertEquals(newDateTime, result.getAppointmentDateTime());
        assertEquals("SCHEDULED", result.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    // Doctor Availability Tests
    @Test
    void getAllDoctors_ShouldReturnAllDoctors() {
        // Arrange
        List<Doctor> doctors = Arrays.asList(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        // Act
        List<Doctor> result = adminService.getAllDoctors();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialization());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorAppointments_ShouldReturnDoctorAppointments() {
        // Arrange
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(appointments);

        // Act
        List<Appointment> result = adminService.getDoctorAppointments(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getDoctor().getId());
        verify(appointmentRepository, times(1)).findByDoctorId(1L);
    }

    @Test
    void isDoctorAvailable_WhenNoConflicts_ShouldReturnTrue() {
        // Arrange
        LocalDateTime checkDateTime = LocalDateTime.now().plusDays(3);
        List<Appointment> doctorAppointments = Arrays.asList(appointment); // Different time
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(doctorAppointments);

        // Act
        boolean result = adminService.isDoctorAvailable(1L, checkDateTime);

        // Assert
        assertTrue(result);
    }

    @Test
    void isDoctorAvailable_WhenTimeConflict_ShouldReturnFalse() {
        // Arrange
        LocalDateTime checkDateTime = appointment.getAppointmentDateTime();
        List<Appointment> doctorAppointments = Arrays.asList(appointment);
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(doctorAppointments);

        // Act
        boolean result = adminService.isDoctorAvailable(1L, checkDateTime);

        // Assert
        assertFalse(result);
    }

    @Test
    void isDoctorAvailable_WhenCancelledAppointment_ShouldReturnTrue() {
        // Arrange
        LocalDateTime checkDateTime = appointment.getAppointmentDateTime();
        appointment.setStatus("CANCELLED");
        List<Appointment> doctorAppointments = Arrays.asList(appointment);
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(doctorAppointments);

        // Act
        boolean result = adminService.isDoctorAvailable(1L, checkDateTime);

        // Assert
        assertTrue(result);
    }

    // Patient Management Tests
    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = adminService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void createPatient_ShouldSaveAndReturnPatient() {
        // Arrange
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        Patient result = adminService.createPatient(patient);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatient_WithValidId_ShouldUpdatePatient() {
        // Arrange
        Patient updatedDetails = Patient.builder()
                .firstName("Johnny")
                .lastName("Doe Updated")
                .email("johnny@email.com")
                .phone("555-1234")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .address("456 New Street")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        Patient result = adminService.updatePatient(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
        assertEquals("johnny@email.com", result.getEmail());
        assertEquals("555-1234", result.getPhone());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatient_WithInvalidId_ShouldReturnNull() {
        // Arrange
        Patient updatedDetails = new Patient();
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Patient result = adminService.updatePatient(999L, updatedDetails);

        // Assert
        assertNull(result);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_WithValidId_ShouldReturnTrue() {
        // Arrange
        when(patientRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = adminService.deletePatient(1L);

        // Assert
        assertTrue(result);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatient_WithInvalidId_ShouldReturnFalse() {
        // Arrange
        when(patientRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = adminService.deletePatient(999L);

        // Assert
        assertFalse(result);
        verify(patientRepository, never()).deleteById(anyLong());
    }

    // Doctor Management Tests
    @Test
    void createDoctor_ShouldSaveAndReturnDoctor() {
        // Arrange
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Act
        Doctor result = adminService.createDoctor(doctor);

        // Assert
        assertNotNull(result);
        assertEquals("Cardiology", result.getSpecialization());
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void updateDoctor_WithValidId_ShouldUpdateDoctor() {
        // Arrange
        Doctor updatedDetails = Doctor.builder()
                .firstName("Michael")
                .lastName("Johnson Updated")
                .specialization("Neurology")
                .qualifications("MD, PhD")
                .experienceYears(20)
                .email("michael.updated@hospital.com")
                .phone("555-5678")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Act
        Doctor result = adminService.updateDoctor(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Neurology", result.getSpecialization());
        assertEquals("MD, PhD", result.getQualifications());
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void deleteDoctor_WithValidId_ShouldReturnTrue() {
        // Arrange
        when(doctorRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = adminService.deleteDoctor(1L);

        // Assert
        assertTrue(result);
        verify(doctorRepository, times(1)).deleteById(1L);
    }

    // Authentication Tests
    @Test
    void authenticateAdmin_WithValidCredentials_ShouldReturnTrue() {
        // Arrange
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        // Act
        boolean result = adminService.authenticateAdmin("admin", "admin123");

        // Assert
        assertTrue(result);
    }

    @Test
    void authenticateAdmin_WithInvalidUsername_ShouldReturnFalse() {
        // Arrange
        when(adminRepository.findByUsername("wrongadmin")).thenReturn(Optional.empty());

        // Act
        boolean result = adminService.authenticateAdmin("wrongadmin", "admin123");

        // Assert
        assertFalse(result);
    }

    @Test
    void authenticateAdmin_WithInvalidPassword_ShouldReturnFalse() {
        // Arrange
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        // Act
        boolean result = adminService.authenticateAdmin("admin", "wrongpassword");

        // Assert
        assertFalse(result);
    }

    // Dashboard Statistics Tests
    @Test
    void getDashboardStats_ShouldReturnCorrectStatistics() {
        // Arrange
        when(patientRepository.count()).thenReturn(10L);
        when(doctorRepository.count()).thenReturn(5L);
        when(appointmentRepository.count()).thenReturn(25L);

        Appointment pendingAppointment = Appointment.builder().status("REQUESTED").build();
        Appointment approvedAppointment = Appointment.builder().status("APPROVED").build();
        List<Appointment> allAppointments = Arrays.asList(pendingAppointment, approvedAppointment);
        when(appointmentRepository.findAll()).thenReturn(allAppointments);

        // Act
        AdminDashboardStats stats = adminService.getDashboardStats();

        // Assert
        assertNotNull(stats);
        assertEquals(10L, stats.getTotalPatients());
        assertEquals(5L, stats.getTotalDoctors());
        assertEquals(25L, stats.getTotalAppointments());
        assertEquals(1L, stats.getPendingAppointments());
        assertEquals(1L, stats.getApprovedAppointments());
    }
}