package com.example.smartseat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_schedule")
public class ExamSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String examName;
    private LocalDateTime examDateTime;
    private boolean active;

    public ExamSchedule() {}

    public Long getId() { return id; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public LocalDateTime getExamDateTime() { return examDateTime; }
    public void setExamDateTime(LocalDateTime examDateTime) {
        this.examDateTime = examDateTime;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}