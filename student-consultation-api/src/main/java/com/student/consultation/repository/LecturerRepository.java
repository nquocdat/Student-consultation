package com.student.consultation.repository;


import com.student.consultation.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

//    Lecturer findByUserId(Long userId);
    Optional<Lecturer> findByUserId(Long userId);
}
