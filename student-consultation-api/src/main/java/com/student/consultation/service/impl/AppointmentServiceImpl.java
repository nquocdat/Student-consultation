package com.student.consultation.service.impl;

import com.student.consultation.dto.AppointmentRequestDTO;
import com.student.consultation.dto.AppointmentResponseDTO;
import com.student.consultation.dto.AppointmentUpdateDTO;
import com.student.consultation.entity.Appointment;
import com.student.consultation.entity.AppointmentStatus;
import com.student.consultation.entity.Status;
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

        if (dto.getDate() != null)
            appointment.setDate(LocalDate.parse(dto.getDate()));

        if (dto.getTime() != null)
            appointment.setTime(LocalTime.parse(dto.getTime()));

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

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByStudent(Long studentId) {
        return appointmentRepository.findByStudentId(studentId)
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByLecturer(Long lecturerId) {
        return appointmentRepository.findByLecturerId(lecturerId)
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
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
}

