package com.student.consultation.service.impl;

import com.student.consultation.entity.Student;
import com.student.consultation.entity.User;
import com.student.consultation.repository.StudentRepository;
import com.student.consultation.repository.UserRepository;
import com.student.consultation.service.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // ==========================================
    // Lấy student theo userId
    // ==========================================
    @Override
    public Student getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // ==========================================
    // Lấy student theo username (dùng cho JWT)
    // ==========================================
    @Override
    public Student getByUsername(String username) {
        return studentRepository.findByUser_Username(username)
                .orElseThrow(() ->
                        new RuntimeException("Student not found for username: " + username));
    }

    // ==========================================
    // Cập nhật thông tin student
    // ==========================================
    @Override
    public Student updateStudent(Long userId, Student updated) {

        Student student = getByUserId(userId);

        student.setFullName(updated.getFullName());
        student.setPhone(updated.getPhone());
        student.setDob(updated.getDob());
        student.setAddress(updated.getAddress());
        student.setGender(updated.getGender());
        student.setClassName(updated.getClassName());
        student.setMajor(updated.getMajor());
        student.setCourse(updated.getCourse());

        return studentRepository.save(student);
    }
}
