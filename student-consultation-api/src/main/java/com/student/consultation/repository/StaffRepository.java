package com.student.consultation.repository;

import com.student.consultation.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Staff findByUserId(Long userId);
}
