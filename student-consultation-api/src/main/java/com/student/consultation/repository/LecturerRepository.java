package com.student.consultation.repository;


import com.student.consultation.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

    Lecturer findByUserId(Long userId);
}
