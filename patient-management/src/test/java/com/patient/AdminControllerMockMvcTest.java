package com.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.admin.dto.AdminDashboardStats;
import com.admin.dto.AdminLoginRequest;
import com.patient.model.*;
import com.admin.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

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

    @Test
    void getAllAppointments_ShouldReturnAppointments() throws Exception {
        // Arrange
        Patient patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        Doctor doctor = Doctor.builder().id(1L).firstName("Michael").lastName("Johnson").build();
        Appointment appointment = Appointment.builder()
                .id(1L)
                .appointmentDateTime(LocalDateTime.now().plusDays(1))
                .status("REQUESTED")
                .patient(patient)
                .doctor(doctor)
                .build();

        List<Appointment> appointments = Arrays.asList(appointment);
        when(adminService.getAllAppointments()).thenReturn(appointments);

        // Act & Assert
        mockMvc.perform(get("/api/admin/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));
    }

    @Test
    void approveAppointment_WithValidId_ShouldReturnApprovedAppointment() throws Exception {
        // Arrange
        Patient patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        Doctor doctor = Doctor.builder().id(1L).firstName("Michael").lastName("Johnson").build();
        Appointment approvedAppointment = Appointment.builder()
                .id(1L)
                .status("APPROVED")
                .patient(patient)
                .doctor(doctor)
                .build();

        when(adminService.approveAppointment(1L)).thenReturn(approvedAppointment);

        // Act & Assert
        mockMvc.perform(put("/api/admin/appointments/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void createPatient_ShouldReturnCreatedPatient() throws Exception {
        // Arrange
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .build();

        when(adminService.createPatient(any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(post("/api/admin/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@email.com"));
    }

    @Test
    void createDoctor_ShouldReturnCreatedDoctor() throws Exception {
        // Arrange
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Michael")
                .lastName("Johnson")
                .specialization("Cardiology")
                .email("michael@hospital.com")
                .build();

        when(adminService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        // Act & Assert
        mockMvc.perform(post("/api/admin/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.email").value("michael@hospital.com"));
    }
}