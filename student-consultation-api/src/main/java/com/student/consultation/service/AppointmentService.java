package com.student.consultation.service;

import com.student.consultation.dto.AppointmentRequestDTO;
import com.student.consultation.dto.AppointmentResponseDTO;
import com.student.consultation.dto.AppointmentUpdateDTO;
import com.student.consultation.entity.Appointment;
import com.student.consultation.entity.Status;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO dto);

    void deleteAppointment(Long id);

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAppointmentsByStudent(Long studentId);

    List<AppointmentResponseDTO> getAppointmentsForLecturer(Long lecturerUserId);


    List<AppointmentResponseDTO> getAppointmentsByStatus(Status status);

    List<AppointmentResponseDTO> getAllAppointments();
    public AppointmentResponseDTO lecturerUpdateStatus(Long appointmentId, Long lecturerUserId, AppointmentUpdateDTO dto);

    // Sinh viên hủy lịch
    void cancelByStudent(Long appointmentId, Long studentId);

    // Giảng viên hủy lịch
    void cancelByLecturer(Long appointmentId, Long lecturerId);
    void approveCancelRequest(Long appointmentId, Long lecturerUserId);
    void rejectCancelRequest(Long appointmentId, Long lecturerUserId);



}
