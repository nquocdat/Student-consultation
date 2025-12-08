package com.student.consultation.controller;

import com.student.consultation.dto.*;
import com.student.consultation.entity.User;
import com.student.consultation.security.JwtUtil;
import com.student.consultation.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // ================================
    // REGISTER
    // ================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User newUser = userService.registerUser(request);
        return ResponseEntity.ok(newUser);
    }

    // ================================
    // LOGIN
    // ================================
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//
//        User user = userService.authenticate(request.getEmail(), request.getPassword());
//
//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//
//        return ResponseEntity.ok(
//                new LoginResponse(token, user.getEmail(), user.getRole().name())
//        );
//    }

    // ================================
    // PROFILE (user đang login)
    // ================================
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication auth) {

        String username = auth.getName(); // lấy từ token
        User user = userService.getByUsername(username);

        return ResponseEntity.ok(user);
    }


    // ================================
    // CHỈ ADMIN ĐƯỢC LẤY DANH SÁCH USER
    // ================================
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> list = userService.getAllUsers();
        return ResponseEntity.ok(list);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        // kiểm tra username + password
        User user = userService.authenticate(request.getUsername(), request.getPassword());

        // tạo token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());


        // trả token + role để frontend biết người này thuộc role nào
        return ResponseEntity.ok(
                new AuthResponse(token, user.getRole().name(), user.getFullName())
        );
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        boolean ok = userService.changePassword(username, request);

        if (!ok) {
            return ResponseEntity.badRequest().body("Old password is incorrect!");
        }
        return ResponseEntity.ok("Password changed successfully!");
    }


}
