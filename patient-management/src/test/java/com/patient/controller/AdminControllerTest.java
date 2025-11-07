package com.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.admin.controller.AdminController;
import com.admin.dto.AdminDashboardStats;
import com.admin.dto.AdminLoginRequest;
import com.admin.dto.AppointmentScheduleRequest;
import com.patient.model.*;
import com.admin.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    private Patient createTestPatient() {
        return Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .build();
    }

    private Doctor createTestDoctor() {
        return Doctor.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("Johnson")
                .specialization("Cardiology")
                .build();
    }

    private Appointment createTestAppointment() {
        return Appointment.builder()
                .id(1L)
                .appointmentDateTime(LocalDateTime.now().plusDays(1))
                .status("REQUESTED")
                .patient(createTestPatient())
                .doctor(createTestDoctor())
                .build();
    }

    // Authentication Tests
    @Test
    void login_WithValidCredentials_ShouldReturnSuccess() throws Exception {
        // Arrange
        AdminLoginRequest loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");
        
        when(adminService.authenticateAdmin("admin", "admin123")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        AdminLoginRequest loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrongpassword");
        
        when(adminService.authenticateAdmin("admin", "wrongpassword")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    // Dashboard Tests
    @Test
    void getDashboardStats_ShouldReturnStats() throws Exception {
        // Arrange
        AdminDashboardStats stats = new AdminDashboardStats();
        stats.setTotalPatients(10L);
        stats.setTotalDoctors(5L);
        stats.setTotalAppointments(25L);
        stats.setPendingAppointments(3L);
        stats.setApprovedAppointments(15L);
        
        when(adminService.getDashboardStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/api/admin/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatients").value(10))
                .andExpect(jsonPath("$.totalDoctors").value(5))
                .andExpect(jsonPath("$.totalAppointments").value(25))
                .andExpect(jsonPath("$.pendingAppointments").value(3))
                .andExpect(jsonPath("$.approvedAppointments").value(15));
    }

    // Appointment Management Tests
    @Test
    void getAllAppointments_ShouldReturnAppointments() throws Exception {
        // Arrange
        List<Appointment> appointments = Arrays.asList(createTestAppointment());
        when(adminService.getAllAppointments()).thenReturn(appointments);

        // Act & Assert
        mockMvc.perform(get("/api/admin/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));
    }

    @Test
    void getPendingAppointments_ShouldReturnPendingAppointments() throws Exception {
        // Arrange
        List<Appointment> appointments = Arrays.asList(createTestAppointment());
        when(adminService.getPendingAppointments()).thenReturn(appointments);

        // Act & Assert
        mockMvc.perform(get("/api/admin/appointments/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));
    }

    @Test
    void approveAppointment_WithValidId_ShouldReturnApprovedAppointment() throws Exception {
        // Arrange
        Appointment approvedAppointment = createTestAppointment();
        approvedAppointment.setStatus("APPROVED");
        when(adminService.approveAppointment(1L)).thenReturn(approvedAppointment);

        // Act & Assert
        mockMvc.perform(put("/api/admin/appointments/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void approveAppointment_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(adminService.approveAppointment(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/admin/appointments/999/approve"))
                .andExpect(status().isNotFound());
    }

    @Test
    void scheduleAppointment_WithValidRequest_ShouldReturnScheduledAppointment() throws Exception {
        // Arrange
        AppointmentScheduleRequest request = new AppointmentScheduleRequest();
        request.setAppointmentId(1L);
        request.setNewDateTime(LocalDateTime.now().plusDays(2));

        Appointment scheduledAppointment = createTestAppointment();
        scheduledAppointment.setStatus("SCHEDULED");
        
        when(adminService.scheduleAppointment(eq(1L), any(LocalDateTime.class)))
                .thenReturn(scheduledAppointment);

        // Act & Assert
        mockMvc.perform(put("/api/admin/appointments/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    // Doctor Management Tests
    @Test
    void getAllDoctors_ShouldReturnDoctors() throws Exception {
        // Arrange
        List<Doctor> doctors = Arrays.asList(createTestDoctor());
        when(adminService.getAllDoctors()).thenReturn(doctors);

        // Act & Assert
        mockMvc.perform(get("/api/admin/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].specialization").value("Cardiology"));
    }

    @Test
    void getDoctorAppointments_ShouldReturnDoctorAppointments() throws Exception {
        // Arrange
        List<Appointment> appointments = Arrays.asList(createTestAppointment());
        when(adminService.getDoctorAppointments(1L)).thenReturn(appointments);

        // Act & Assert
        mockMvc.perform(get("/api/admin/doctors/1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void checkDoctorAvailability_WhenAvailable_ShouldReturnTrue() throws Exception {
        // Arrange
        when(adminService.isDoctorAvailable(eq(1L), any(LocalDateTime.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/admin/doctors/1/availability")
                .param("dateTime", LocalDateTime.now().plusDays(1)
                .truncatedTo(ChronoUnit.SECONDS).toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // Patient Management Tests
    @Test
    void getAllPatients_ShouldReturnPatients() throws Exception {
        // Arrange
        List<Patient> patients = Arrays.asList(createTestPatient());
        when(adminService.getAllPatients()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("/api/admin/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void createPatient_ShouldReturnCreatedPatient() throws Exception {
        // Arrange
        Patient patient = createTestPatient();
        when(adminService.createPatient(any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(post("/api/admin/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updatePatient_WithValidId_ShouldReturnUpdatedPatient() throws Exception {
        // Arrange
        Patient patient = createTestPatient();
        when(adminService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(put("/api/admin/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updatePatient_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        Patient patient = createTestPatient();
        when(adminService.updatePatient(eq(999L), any(Patient.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/admin/patients/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePatient_WithValidId_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(adminService.deletePatient(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted successfully"));
    }

    @Test
    void deletePatient_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(adminService.deletePatient(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/patients/999"))
                .andExpect(status().isNotFound());
    }

    // Doctor CRUD Tests
    @Test
    void createDoctor_ShouldReturnCreatedDoctor() throws Exception {
        // Arrange
        Doctor doctor = createTestDoctor();
        when(adminService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        // Act & Assert
        mockMvc.perform(post("/api/admin/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialization").value("Cardiology"));
    }

    @Test
    void deleteDoctor_WithValidId_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(adminService.deleteDoctor(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted successfully"));
    }

    // Notification Tests
    @Test
    void sendAppointmentNotification_ShouldReturnSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/admin/notifications/appointment")
                .param("appointmentId", "1")
                .param("message", "Your appointment has been scheduled"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent successfully"));
    }
}