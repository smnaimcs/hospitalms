package com.patient.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patient.dto.AppointmentRequestDTO;
import com.patient.model.Appointment;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.patient.repository.AppointmentRepository;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public Appointment createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        Optional<Patient> patient = patientService.getPatientById(appointmentRequestDTO.getPatientId());
        Optional<Doctor> doctor = doctorService.getDoctorById(appointmentRequestDTO.getDoctorId());

        if (patient.isPresent() && doctor.isPresent()) {
            Appointment appointment = new Appointment();
            appointment.setPatient(patient.get());
            appointment.setDoctor(doctor.get());
            appointment.setAppointmentDateTime(appointmentRequestDTO.getAppointmentDateTime());
            appointment.setStatus("SCHEDULED");

            return appointmentRepository.save(appointment);
        }

        return null;
    }

    public boolean cancelAppointment(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus("CANCELLED");
            appointmentRepository.save(appointment);
            return true;
        }

        return false;
    }
}
