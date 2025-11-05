package com.patient.controller;

import com.doctor.controller.DoctorDashboardController;
import com.doctor.model.Diagnosis;
import com.doctor.model.Prescription;
import com.doctor.repository.PrescriptionService;
import com.doctor.service.DiagnosisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.model.*;
import com.patient.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorDashboardController.class)
class DoctorDashboardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @MockBean
    private PatientService patientService;

    @MockBean
    private DiagnosisService diagnosisService;

    @MockBean
    private PrescriptionService prescriptionService;

    @Test
    void getDoctorAppointments_ShouldReturnAppointments() throws Exception {
        // Arrange
        Patient patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        Doctor doctor = Doctor.builder().id(1L).firstName("Michael").lastName("Johnson").build();
        Appointment appointment = Appointment.builder()
                .id(1L)
                .patient(patient)
                .doctor(doctor)
                .status("SCHEDULED")
                .build();

        when(appointmentService.getAppointmentsByDoctorId(1L))
                .thenReturn(Arrays.asList(appointment));

        // Act & Assert
        mockMvc.perform(get("/api/doctor/1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void addDiagnosis_WithValidData_ShouldCreateDiagnosis() throws Exception {
        // Arrange
        Diagnosis diagnosis = Diagnosis.builder()
                .diagnosisCode("I10")
                .diagnosisName("Hypertension")
                .description("High blood pressure")
                .build();

        when(diagnosisService.addDiagnosis(anyLong(), anyLong(), any(Diagnosis.class)))
                .thenReturn(diagnosis);

        // Act & Assert
        mockMvc.perform(post("/api/doctor/1/patients/1/diagnosis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diagnosis)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosisCode").value("I10"))
                .andExpect(jsonPath("$.diagnosisName").value("Hypertension"));
    }

    @Test
    void prescribeMedicine_WithValidData_ShouldCreatePrescription() throws Exception {
        // Arrange
        Prescription prescription = Prescription.builder()
                .medicineName("Lisinopril")
                .doses("10mg")
                .frequency("Once daily")
                .durationDays(30)
                .instructions("Take in morning")
                .build();

        when(prescriptionService.prescribeMedicine(anyLong(), anyLong(), any(Prescription.class)))
                .thenReturn(prescription);

        // Act & Assert
        mockMvc.perform(post("/api/doctor/1/patients/1/prescription")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prescription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicineName").value("Lisinopril"))
                .andExpect(jsonPath("$.doses").value("10mg"));
    }

    @Test
    void searchPatients_ShouldReturnMatchingPatients() throws Exception {
        // Arrange
        Patient patient = Patient.builder().id(1L).firstName("John").lastName("Doe").build();
        when(patientService.searchPatients("John")).thenReturn(Arrays.asList(patient));

        // Act & Assert
        mockMvc.perform(get("/api/doctor/1/search/patients?query=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}