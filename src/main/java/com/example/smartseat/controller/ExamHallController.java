package com.example.smartseat.controller;

import com.example.smartseat.entity.ExamHall;
import com.example.smartseat.service.ExamHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/halls")
public class ExamHallController {

    @Autowired
    private ExamHallService service;

    @PostMapping
    public ExamHall addHall(@RequestBody ExamHall hall) {
        return service.saveHall(hall);
    }

    @GetMapping
    public List<ExamHall> getHalls() {
        return service.getAllHalls();
    }
}
