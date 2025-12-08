package com.student.consultation.controller;

import com.student.consultation.entity.Schedule;
import com.student.consultation.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create/{lecturerId}")
    public ResponseEntity<?> createSchedule(
            @PathVariable Long lecturerId,
            @RequestBody Schedule schedule
    ) {
        return ResponseEntity.ok(scheduleService.createSchedule(lecturerId, schedule));
    }

    @GetMapping("/free/{lecturerId}")
    public ResponseEntity<?> getFreeSlots(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(scheduleService.getLecturerFreeSlots(lecturerId));
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<?> getLecturerSchedules(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(scheduleService.getLecturerSchedules(lecturerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("Schedule deleted");
    }
}
