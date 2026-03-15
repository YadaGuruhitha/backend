package com.example.smartseat.service;

import com.example.smartseat.entity.Seating;
import com.example.smartseat.repository.SeatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SeatingService {

    @Autowired
    private SeatingRepository seatingRepository;

    public void generateSeating(Map<String, String> data) {

        // Clear all previous seating data
        seatingRepository.deleteAll();

        String[] sections = data.get("sections").split(",");
        int totalStudents = Integer.parseInt(data.get("totalStudents"));
        String roomName   = data.get("roomName");
        int capacity      = Integer.parseInt(data.get("capacity"));
        int rooms         = Integer.parseInt(data.get("rooms"));

        // BUG FIX: distribute students evenly across ALL sections.
        // Remainder students are given to the first sections one by one.
        int base      = totalStudents / sections.length;
        int remainder = totalStudents % sections.length;

        // Step 1: Build a flat ordered list of every student ID across all sections
        List<String> allStudentIds = new ArrayList<>();
        for (int s = 0; s < sections.length; s++) {
            String section      = sections[s].trim();
            int countForSection = base + (s < remainder ? 1 : 0);
            for (int i = 1; i <= countForSection; i++) {
                allStudentIds.add(section + String.format("%03d", i));
            }
        }

        // Step 2: Assign seats sequentially from the flat list.
        // hallNumber is NEVER reset to 1 — this was the original bug that caused
        // students from different sections to get the same hall+seat combination.
        int seatNumber = 1;
        int hallNumber = 1;

        List<Seating> seatingList = new ArrayList<>();

        for (String studentId : allStudentIds) {
            Seating seating = new Seating();
            seating.setStudentId(studentId);
            seating.setHallId(roomName + hallNumber);
            seating.setSeatNumber(seatNumber);
            seatingList.add(seating);

            seatNumber++;
            if (seatNumber > capacity) {
                seatNumber = 1;
                hallNumber++;
                // If total students exceed rooms * capacity,
                // extra halls (Hall4, Hall5 ...) are created automatically.
            }
        }

        seatingRepository.saveAll(seatingList);
    }
}
