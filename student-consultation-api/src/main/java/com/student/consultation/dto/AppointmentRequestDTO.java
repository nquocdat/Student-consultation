package com.student.consultation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {
    private Long lecturerId;
    private Long studentId;
    private LocalDate date;
    private LocalTime time;
    private String reason;
}
