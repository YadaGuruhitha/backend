package com.example.smartseat.controller;

import com.example.smartseat.entity.Seating;
import com.example.smartseat.repository.SeatingRepository;
import com.example.smartseat.service.SeatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seating")
public class SeatingController {

    @Autowired
    private SeatingService seatingService;

    @Autowired
    private SeatingRepository seatingRepository;

    @PostMapping("/generate")
    public String generateSeating(@RequestBody Map<String, String> data) {
        seatingService.generateSeating(data);
        return "Seating Generated Successfully";
    }

    @GetMapping("/all")
    public List<Seating> getAllSeating() {
        return seatingRepository.findAll();
    }
}
