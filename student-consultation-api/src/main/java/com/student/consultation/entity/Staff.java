package com.student.consultation.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position; // Ví dụ: Tiếp nhận lịch, xử lý hồ sơ…

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
