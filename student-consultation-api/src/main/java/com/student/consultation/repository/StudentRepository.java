package com.student.consultation.repository;


import com.student.consultation.entity.Student;
import com.student.consultation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    Optional<Student> findByUser(User user);
    Optional<Student> findByStudentCode(String studentCode);
    Optional<Student> findByUser_Username(String username);
}
