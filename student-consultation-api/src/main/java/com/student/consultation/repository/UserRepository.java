package com.student.consultation.repository;

import com.student.consultation.entity.Role;
import com.student.consultation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

}
