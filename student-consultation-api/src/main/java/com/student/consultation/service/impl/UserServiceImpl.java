package com.student.consultation.service.impl;

import com.student.consultation.dto.ChangePasswordRequest;
import com.student.consultation.dto.RegisterRequest;
import com.student.consultation.entity.Role;
import com.student.consultation.entity.Student;
import com.student.consultation.entity.User;
import com.student.consultation.repository.UserRepository;
import com.student.consultation.service.UserService;
import com.student.consultation.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    // REGISTER
    @Override
    public User registerUser(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // Convert String -> Enum
        user.setRole(Role.valueOf(req.getRole().toUpperCase()));

        user.setFullName(req.getFullName());

        User savedUser = userRepository.save(user);

        // Tạo student nếu role = STUDENT
        if (savedUser.getRole() == Role.STUDENT) {

            Student s = new Student();
            s.setUser(savedUser);
            s.setFullName(savedUser.getFullName());
            s.setEmail(savedUser.getEmail());
            s.setStudentCode(savedUser.getUsername());

            studentRepository.save(s);
        }

        return savedUser;
    }




    // AUTHENTICATE
    @Override
    public User authenticate(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }


    // GET BY EMAIL
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // GET ALL
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public boolean changePassword(String username, ChangePasswordRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return false; // mật khẩu cũ sai
        }

        // Mã hoá mật khẩu mới
        String encodedNewPass = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPass);

        userRepository.save(user);
        return true;
    }

}
