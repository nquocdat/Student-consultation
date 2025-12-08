package com.student.consultation.controller;

import com.student.consultation.entity.Student;
import com.student.consultation.security.JwtUtil;
import com.student.consultation.service.impl.StudentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;
    private final JwtUtil jwtUtil;


    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication auth) {

        String username = auth.getName();
        Student student = studentService.getByUsername(username);

        return ResponseEntity.ok(student);
    }


}
