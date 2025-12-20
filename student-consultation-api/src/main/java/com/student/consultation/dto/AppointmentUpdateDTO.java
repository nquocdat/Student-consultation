package com.student.consultation.dto;

import com.student.consultation.entity.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentUpdateDTO {
    private LocalDate date;
    private LocalTime time;
    private String reason;
    private String lecturerNote;
    private Status status;
}
