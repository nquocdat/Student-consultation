package com.student.consultation.repository;

import com.student.consultation.entity.Appointment;
import com.student.consultation.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStudentId(Long studentId);
    List<Appointment> findByLecturerId(Long lecturerId);
    List<Appointment> findByStatus(Status status);

    boolean existsByStudent_IdAndDateAndTime(Long studentId, LocalDate date, LocalTime time);
    // lấy sanh sách lịch hẹn của sinh viên
    List<Appointment> findByStudent_Id(Long studentId);
    List<Appointment> findByLecturer_Id(Long lecturerId);


}
