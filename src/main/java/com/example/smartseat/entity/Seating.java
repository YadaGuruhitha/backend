package com.example.smartseat.entity;

import jakarta.persistence.*;

@Entity
public class Seating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String hallId;
    private int seatNumber;

    public Seating() {}

    public Long getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getHallId() { return hallId; }
    public int getSeatNumber() { return seatNumber; }

    public void setId(Long id) { this.id = id; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setHallId(String hallId) { this.hallId = hallId; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}
