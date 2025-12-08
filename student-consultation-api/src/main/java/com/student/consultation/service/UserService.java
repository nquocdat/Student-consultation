package com.student.consultation.service;

import com.student.consultation.dto.ChangePasswordRequest;
import com.student.consultation.dto.RegisterRequest;
import com.student.consultation.entity.User;

import java.util.List;

public interface UserService {

    User registerUser(RegisterRequest request);

    User authenticate(String email, String rawPassword);

    User getByEmail(String email);

    List<User> getAllUsers();
    User getByUsername(String username);
    boolean changePassword(String username, ChangePasswordRequest request);


}
