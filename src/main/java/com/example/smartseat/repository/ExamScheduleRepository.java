package com.example.smartseat.repository;

import com.example.smartseat.entity.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
    Optional<ExamSchedule> findByActiveTrue();
}