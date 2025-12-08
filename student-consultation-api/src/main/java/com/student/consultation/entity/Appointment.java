package com.student.consultation.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;     // người đặt lịch

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;   // giảng viên

    private LocalDate date;
    private LocalTime time;


    @Enumerated(EnumType.STRING)
    private Status status;       // PENDING, APPROVED, REJECTED, COMPLETED

    private String reason;       // Nội dung yêu cầu tư vấn
}