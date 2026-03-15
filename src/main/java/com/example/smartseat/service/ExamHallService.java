package com.example.smartseat.service;

import com.example.smartseat.entity.ExamHall;
import com.example.smartseat.repository.ExamHallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamHallService {

    @Autowired
    private ExamHallRepository repository;

    public ExamHall saveHall(ExamHall hall) {
        return repository.save(hall);
    }

    public List<ExamHall> getAllHalls() {
        return repository.findAll();
    }
}
