package com.student.consultation.service;

import com.student.consultation.entity.Student;

public interface StudentService {
    Student getByUserId(Long userId);
    Student getByUsername(String username);
    Student updateStudent(Long userId, Student updated);
}
