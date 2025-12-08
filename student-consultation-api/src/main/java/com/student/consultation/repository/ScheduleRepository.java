package com.student.consultation.repository;

import com.student.consultation.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Lấy toàn bộ lịch rảnh / bận của giảng viên
    List<Schedule> findByLecturerId(Long lecturerId);

    // Lấy lịch theo ngày
    List<Schedule> findByLecturerIdAndDate(Long lecturerId, LocalDate date);

    /**
     * Kiểm tra giảng viên có sẵn thời gian (còn available)
     * và khung giờ yêu cầu nằm TRONG khung giờ rảnh.
     *
     * Ví dụ:
     *  Giảng viên rảnh từ 09:00 - 11:00
     *  Sinh viên đặt 09:30 → OK
     */
    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
        FROM Schedule s
        WHERE s.lecturer.id = :lecturerId
          AND s.date = :date
          AND s.startTime <= :time
          AND s.endTime >= :time
          AND s.available = TRUE
        """)
    boolean existsAvailableSlot(
            Long lecturerId,
            LocalDate date,
            LocalTime time
    );


    /**
     * Lấy toàn bộ khung giờ rảnh theo ngày
     */
    @Query("""
        SELECT s 
        FROM Schedule s
        WHERE s.lecturer.id = :lecturerId
          AND s.date = :date
          AND s.available = TRUE
        ORDER BY s.startTime ASC
    """)
    List<Schedule> findFreeSlots(Long lecturerId, LocalDate date);
}
