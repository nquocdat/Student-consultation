package com.student.consultation.mapper;

import com.student.consultation.dto.AppointmentRequestDTO;
import com.student.consultation.dto.AppointmentResponseDTO;
import com.student.consultation.entity.Appointment;

public class AppointmentMapper {

    public static AppointmentResponseDTO toDTO(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .studentId(appointment.getStudent().getId())
                .lecturerId(appointment.getLecturer().getId())
                .date(appointment.getDate())
                .time(appointment.getTime())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .build();
    }
}
