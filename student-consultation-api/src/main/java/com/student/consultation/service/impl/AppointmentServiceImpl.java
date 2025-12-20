package com.student.consultation.service.impl;

import com.student.consultation.dto.AppointmentRequestDTO;
import com.student.consultation.dto.AppointmentResponseDTO;
import com.student.consultation.dto.AppointmentUpdateDTO;
import com.student.consultation.entity.*;
import com.student.consultation.mapper.AppointmentMapper;
import com.student.consultation.repository.AppointmentRepository;
import com.student.consultation.repository.LecturerRepository;
import com.student.consultation.repository.ScheduleRepository;
import com.student.consultation.repository.StudentRepository;
import com.student.consultation.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.student.consultation.security.SecurityUtil;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final SecurityUtil securityUtil;
    private final AppointmentRepository appointmentRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {

        Long currentStudentId = dto.getStudentId();

        var student = studentRepository.findById(currentStudentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        var lecturer = lecturerRepository.findById(dto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        boolean lecturerFree = scheduleRepository
                .existsAvailableSlot(lecturer.getId(), dto.getDate(), dto.getTime());

        if (!lecturerFree) {
            throw new RuntimeException("Lecturer is not available at this time slot");
        }

        boolean hasAnotherAppointment = appointmentRepository
                .existsByStudent_IdAndDateAndTime(student.getId(), dto.getDate(), dto.getTime());


        if (hasAnotherAppointment) {
            throw new RuntimeException("Student already has an appointment at this time");
        }

        Appointment appointment = Appointment.builder()
                .student(student)
                .lecturer(lecturer)
                .date(dto.getDate())
                .time(dto.getTime())
                .reason(dto.getReason())
                .status(Status.PENDING)
                .build();

        appointmentRepository.save(appointment);

        return AppointmentMapper.toDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

//        if (dto.getDate() != null)
//            appointment.setDate(LocalDate.parse(dto.getDate()));
//
//        if (dto.getTime() != null)
//            appointment.setTime(LocalTime.parse(dto.getTime()));
        if (dto.getDate() != null) {
            appointment.setDate(dto.getDate());
        }

        if (dto.getTime() != null) {
            appointment.setTime(dto.getTime());
        }


        if (dto.getReason() != null) appointment.setReason(dto.getReason());
        if (dto.getStatus() != null) appointment.setStatus(dto.getStatus());

        appointmentRepository.save(appointment);
        return AppointmentMapper.toDTO(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    // lấy danh sách lịch hẹn của sinh viên
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByStudent(Long studentUserId) {

        // Lấy student theo user_id
        Student student = studentRepository.findByUserId(studentUserId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Appointment> appointments = appointmentRepository.findByStudent_Id(student.getId());

        return appointments.stream()
                .map(AppointmentResponseDTO::fromEntity)
                .toList();
    }


    @Override
    public List<AppointmentResponseDTO> getAppointmentsForLecturer(Long lecturerUserId) {

        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        Long lecturerId = lecturer.getId();

        return appointmentRepository.findByLecturer_Id(lecturerId)
                .stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
    }



    @Override
    public List<AppointmentResponseDTO> getAppointmentsByStatus(Status status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public AppointmentResponseDTO lecturerUpdateStatus(
            Long appointmentId,
            Long lecturerUserId,
            AppointmentUpdateDTO dto
    ) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        if (!appointment.getLecturer().getId().equals(lecturer.getId())) {
            throw new RuntimeException("You are not allowed to update this appointment");
        }

        Status current = appointment.getStatus();
        Status next = dto.getStatus();

        // 🔥 RULE 1: Completed và Cancelled thì không sửa nữa
        if (current == Status.COMPLETED || current == Status.CANCELLED) {
            throw new RuntimeException("This appointment can no longer be modified");
        }

        // 🔥 RULE 2: PENDING → cho phép APPROVED / REJECTED / CANCELLED
        if (current == Status.PENDING) {
            if (!(next == Status.APPROVED ||
                    next == Status.REJECTED ||
                    next == Status.CANCELLED)) {
                throw new RuntimeException("Invalid transition from PENDING");
            }
        }

        // 🔥 RULE 3: APPROVED → giảng viên chỉ được CANCELLED hoặc COMPLETED
        if (current == Status.APPROVED) {
            if (!(next == Status.CANCELLED || next == Status.COMPLETED)) {
                throw new RuntimeException("Approved appointment can only be cancelled or completed");
            }
        }

        // 🔥 RULE 4: CANCEL_REQUEST → Giảng viên quyết định APPROVED hoặc CANCELLED
        if (current == Status.CANCEL_REQUEST) {
            if (!(next == Status.APPROVED || next == Status.CANCELLED)) {
                throw new RuntimeException("Lecturer must choose APPROVED or CANCELLED for cancel request");
            }
        }

        // Ghi chú của giảng viên nếu có
        if (dto.getReason() != null) {
            appointment.setReason(dto.getReason());
        }


        appointment.setStatus(next);
        appointmentRepository.save(appointment);

        return AppointmentMapper.toDTO(appointment);
    }

    // student hủy lịch khi đã được phê duyệt

    @Override
    public void cancelByStudent(Long appointmentId, Long studentUserId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Student student = studentRepository.findByUserId(studentUserId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Kiểm tra đúng sinh viên
        if (!appointment.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("You are not allowed to cancel this appointment");
        }

        Status currentStatus = appointment.getStatus();

        // Không cho hủy khi đã hoàn thành
        if (currentStatus == Status.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed appointment");
        }

        // Không cho hủy nếu đã bị từ chối
        if (currentStatus == Status.REJECTED) {
            throw new RuntimeException("Appointment was already rejected");
        }

        // Nếu đang PENDING → hủy luôn
        if (currentStatus == Status.PENDING) {
            appointment.setStatus(Status.CANCELLED);
        }
        // Nếu đã APPROVED → gửi yêu cầu hủy
        else if (currentStatus == Status.APPROVED) {
            appointment.setStatus(Status.CANCEL_REQUEST);
        }
        // Các trạng thái khác
        else {
            throw new RuntimeException("Appointment cannot be cancelled in current status");
        }

        appointmentRepository.save(appointment);
    }


    // lecturer chủ động hủy lịch
    @Override
    public void cancelByLecturer(Long appointmentId, Long lecturerUserId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        if (!appointment.getLecturer().getId().equals(lecturer.getId())) {
            throw new RuntimeException("You are not allowed to cancel this appointment");
        }

        Status status = appointment.getStatus();

        if (status == Status.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed appointment");
        }

        if (status == Status.REJECTED || status == Status.CANCELLED) {
            throw new RuntimeException("Appointment already ended");
        }

        // Giảng viên chỉ được chủ động hủy khi PENDING
        if (status != Status.PENDING) {
            throw new RuntimeException("Lecturer can only cancel pending appointments directly");
        }

        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    // lecturere chấp nhận yêu cầu hủy lịch
    @Override
    public void approveCancelRequest(Long appointmentId, Long lecturerUserId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        if (!appointment.getLecturer().getId().equals(lecturer.getId())) {
            throw new RuntimeException("You are not allowed");
        }

        if (appointment.getStatus() != Status.CANCEL_REQUEST) {
            throw new RuntimeException("No cancel request to approve");
        }

        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }
    // lecturer từ chối yêu cầu hủy lịch
    @Override
    public void rejectCancelRequest(Long appointmentId, Long lecturerUserId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        if (!appointment.getLecturer().getId().equals(lecturer.getId())) {
            throw new RuntimeException("You are not allowed");
        }

        if (appointment.getStatus() != Status.CANCEL_REQUEST) {
            throw new RuntimeException("No cancel request to reject");
        }

        appointment.setStatus(Status.APPROVED);
        appointmentRepository.save(appointment);
    }




}

