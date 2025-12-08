package com.student.consultation.service.impl;

import com.student.consultation.entity.Lecturer;
import com.student.consultation.entity.Schedule;
import com.student.consultation.repository.LecturerRepository;
import com.student.consultation.repository.ScheduleRepository;
import com.student.consultation.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final LecturerRepository lecturerRepository;

    @Override
    public Schedule createSchedule(Long lecturerId, Schedule schedule) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        schedule.setLecturer(lecturer);
        schedule.setAvailable(true);

        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getLecturerSchedules(Long lecturerId) {
        return scheduleRepository.findByLecturerId(lecturerId);
    }

    @Override
    public List<Schedule> getLecturerFreeSlots(Long lecturerId) {
        return scheduleRepository.findByLecturerId(lecturerId)
                .stream()
                .filter(Schedule::isAvailable)
                .toList();
    }

    @Override
    public Schedule getById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
