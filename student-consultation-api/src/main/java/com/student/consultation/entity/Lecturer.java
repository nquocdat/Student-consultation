package com.student.consultation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lecturers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;        // Họ tên giảng viên

    private String email;           // Email liên hệ
    private String phone;           // Số điện thoại

    private String department;      // Khoa (CNTT, Kinh tế...)

    private String position;        // Chức vụ (Trưởng khoa, Giảng viên chính...)
    private String academicRank;    // Học hàm (PGS, GS...)
    private String academicDegree;  // Học vị (Tiến sĩ, Thạc sĩ...)

    private String office;          // Phòng làm việc
    private String description;     // Mô tả / giới thiệu giảng viên

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;              // Tài khoản đăng nhập

    @OneToMany(mappedBy = "lecturer")
    @JsonIgnore
    private List<Schedule> schedules;


}
