package com.doctor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doctor.model.Diagnosis;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByPatientId(Long patientId);
    List<Diagnosis> findByDoctorId(Long doctorId);

    @Query("SELECT d FROM Diagnosis d WHERE d.doctor.id = :doctorId AND d.patient.id = :patientId")
    List<Diagnosis> findByDoctorAndPatient(@Param("doctorId") Long doctorId, @Param("patientId") Long patientId);
}
