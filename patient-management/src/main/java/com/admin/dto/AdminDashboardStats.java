package com.admin.dto;

import lombok.Data;

@Data
public class AdminDashboardStats {
    private Long totalPatients;
    private Long totalDoctors;
    private Long totalAppointments;
    private Long pendingAppointments;
    private Long approvedAppointments;
}
