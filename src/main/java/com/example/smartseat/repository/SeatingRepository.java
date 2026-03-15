package com.example.smartseat.repository;

import com.example.smartseat.entity.Seating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatingRepository extends JpaRepository<Seating, Long> {
}
