package com.patient;

import com.patient.dto.AppointmentRequestDTO;
import com.patient.model.Appointment;
import com.patient.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PatientManagementE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    void completePatientAppointmentFlow_ShouldWork() {
        // 1. Get all patients
        ResponseEntity<Patient[]> patientsResponse = restTemplate.getForEntity(
                getBaseUrl() + "/patients", Patient[].class);
        
        assertEquals(HttpStatus.OK, patientsResponse.getStatusCode());
        assertTrue(patientsResponse.getBody().length > 0);

        // 2. Create appointment
        AppointmentRequestDTO appointmentRequest = new AppointmentRequestDTO();
        appointmentRequest.setPatientId(1L);
        appointmentRequest.setDoctorId(1L);
        appointmentRequest.setAppointmentDateTime(LocalDateTime.now().plusDays(1));

        ResponseEntity<Appointment> appointmentResponse = restTemplate.postForEntity(
                getBaseUrl() + "/appointments", appointmentRequest, Appointment.class);
        
        assertEquals(HttpStatus.OK, appointmentResponse.getStatusCode());
        assertNotNull(appointmentResponse.getBody().getId());

        // 3. Verify appointment was created
        ResponseEntity<Appointment[]> appointmentsResponse = restTemplate.getForEntity(
                getBaseUrl() + "/appointments/patient/1", Appointment[].class);
        
        assertEquals(HttpStatus.OK, appointmentsResponse.getStatusCode());
        assertTrue(appointmentsResponse.getBody().length > 0);
    }
}