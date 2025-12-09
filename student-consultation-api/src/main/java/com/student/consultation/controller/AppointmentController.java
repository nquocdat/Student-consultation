package com.student.consultation.controller;

import com.student.consultation.dto.AppointmentRequestDTO;
import com.student.consultation.dto.AppointmentResponseDTO;
import com.student.consultation.dto.AppointmentUpdateDTO;
import com.student.consultation.entity.Appointment;
import com.student.consultation.entity.Status;
import com.student.consultation.entity.Student;
import com.student.consultation.repository.StudentRepository;
import com.student.consultation.security.CustomUserDetails;
import com.student.consultation.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final StudentRepository studentRepository;

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        // Lấy userId từ token
        Long userId = user.getUserId();

        // Lấy student theo userId
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Gán studentId đúng
        req.setStudentId(student.getId());

        AppointmentResponseDTO created = appointmentService.createAppointment(req);

        return ResponseEntity.ok(created);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<AppointmentResponseDTO> update(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateDTO dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<List<AppointmentResponseDTO>> getByStudent(@PathVariable Long studentId) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByStudent(studentId));
//    }

//    @GetMapping("/lecturer/{lecturerId}")
//    public ResponseEntity<List<AppointmentResponseDTO>> getByLecturer(@PathVariable Long lecturerId) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByLecturer(lecturerId));
//    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Deleted");
    }
    // lấy danh sachs lịch hẹn của sinh viên
    @GetMapping("/my")
    public ResponseEntity<?> getMyAppointments(@AuthenticationPrincipal CustomUserDetails user) {

        Long studentUserId = user.getUserId();  // Lấy user_id trong bảng users

        List<AppointmentResponseDTO> list = appointmentService.getAppointmentsByStudent(studentUserId);

        return ResponseEntity.ok(list);
    }
    // lecturer xem danh sách sinh viên đã đặt
    @GetMapping("/lecturer/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getLecturerAppointments(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long lecturerUserId = user.getUserId();

        List<AppointmentResponseDTO> list =
                appointmentService.getAppointmentsForLecturer(lecturerUserId);

        return ResponseEntity.ok(list);
    }



}
