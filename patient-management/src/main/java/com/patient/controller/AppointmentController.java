package com.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patient.dto.AppointmentRequestDTO;
import com.patient.model.Appointment;
import com.patient.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/patient/{patient_id}")
    public List<Appointment> getAppointmentByPatientId(@PathVariable Long patient_id) {
        return appointmentService.getAppointmentsByPatientId(patient_id);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = appointmentService.createAppointment(appointmentRequestDTO);
        if (appointment != null) {
            return ResponseEntity.ok(appointment);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        boolean cancelled = appointmentService.cancelAppointment(id);
        
        if (cancelled) {
            return ResponseEntity.ok("Appointment Cancelled successfully.");
        }

        return ResponseEntity.notFound().build();
    }
}
