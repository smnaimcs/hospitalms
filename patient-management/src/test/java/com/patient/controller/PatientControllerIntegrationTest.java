package com.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.dto.PatientUpdateDTO;
import com.patient.model.Patient;
import com.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @Test
    void getAllPatients_ShouldReturnPatients() throws Exception {
        // Arrange
        Patient patient1 = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        Patient patient2 = Patient.builder().id(2L).firstName("Jane").lastName("Smith").build();
        
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient1, patient2));

        // Act & Assert
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void getPatientById_WithValidId_ShouldReturnPatient() throws Exception {
        // Arrange
        Patient patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getPatientById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(patientService.getPatientById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePatient_WithValidData_ShouldUpdatePatient() throws Exception {
        // Arrange
        PatientUpdateDTO updateDTO = new PatientUpdateDTO();
        updateDTO.setFirstName("Johnny");
        updateDTO.setPhone("555-1234");

        Patient updatedPatient = Patient.builder()
                .id(1L)
                .firstName("Johnny")
                .lastName("Doe")
                .phone("555-1234")
                .build();

        when(patientService.updatePatient(anyLong(), any(PatientUpdateDTO.class)))
                .thenReturn(updatedPatient);

        // Act & Assert
        mockMvc.perform(put("/api/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.phone").value("555-1234"));
    }

    @Test
    void createPatient_ShouldCreatePatient() throws Exception {
        // Arrange
        Patient newPatient = Patient.builder()
                .firstName("New")
                .lastName("Patient")
                .email("new@email.com")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        when(patientService.createPatient(any(Patient.class))).thenReturn(newPatient);

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("New"));
    }
}