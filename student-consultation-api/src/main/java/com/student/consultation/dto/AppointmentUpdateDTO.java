package com.student.consultation.dto;

import com.student.consultation.entity.Status;
import lombok.Data;

@Data
public class AppointmentUpdateDTO {
    private String date;
    private String time;
    private String reason;
    private Status status;
}
