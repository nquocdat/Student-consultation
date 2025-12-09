package com.student.consultation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.student.consultation.entity.Appointment;
import com.student.consultation.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponseDTO {
    private Long id;
    private Long studentId;
    private Long lecturerId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private Status status;
    private String reason;
    public static AppointmentResponseDTO fromEntity(Appointment a) {
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .lecturerId(a.getLecturer().getId())
                .date(a.getDate())
                .time(a.getTime())
                .reason(a.getReason())
                .status(a.getStatus())
                .build();
    }

}
