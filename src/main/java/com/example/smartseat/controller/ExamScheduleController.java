package com.example.smartseat.controller;

import com.example.smartseat.entity.ExamSchedule;
import com.example.smartseat.repository.ExamScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam-schedule")
public class ExamScheduleController {

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    // Get all schedules
    @GetMapping
    public List<ExamSchedule> getAll() {
        return examScheduleRepository.findAll();
    }

    // Get active exam
    @GetMapping("/active")
    public ResponseEntity<?> getActive() {
        return examScheduleRepository.findByActiveTrue()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(404)
                .body(Map.of("message", "No active exam")));
    }

    // Admin sets exam schedule
    // Body: { "examName": "Mid Sem", "examDateTime": "2026-03-15T10:00:00" }
    @PostMapping
    public ResponseEntity<ExamSchedule> setSchedule(
            @RequestBody Map<String, String> body) {

        // Deactivate all previous exams
        examScheduleRepository.findAll().forEach(e -> {
            e.setActive(false);
            examScheduleRepository.save(e);
        });

        ExamSchedule exam = new ExamSchedule();
        exam.setExamName(body.get("examName"));
        exam.setExamDateTime(LocalDateTime.parse(body.get("examDateTime")));
        exam.setActive(true);

        return ResponseEntity.ok(examScheduleRepository.save(exam));
    }

    // Delete schedule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examScheduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}