package com.student.consultation.service;

import com.student.consultation.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    Schedule createSchedule(Long lecturerId, Schedule schedule);

    List<Schedule> getLecturerSchedules(Long lecturerId);

    List<Schedule> getLecturerFreeSlots(Long lecturerId);

    Schedule getById(Long id);

    void deleteSchedule(Long id);
}
