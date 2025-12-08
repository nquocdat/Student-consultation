package com.student.consultation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    @JsonIgnoreProperties({"schedules"})
    private Lecturer lecturer;



    private LocalDate date;       // Ngày rảnh
    private LocalTime startTime;  // Giờ bắt đầu
    private LocalTime endTime;    // Giờ kết thúc

    private boolean available;    // true = còn trống, false = đã có người đặt
}
