package com.admin.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AppointmentScheduleRequest {
    private Long appointmentId;
    private LocalDateTime newDateTime;
}
