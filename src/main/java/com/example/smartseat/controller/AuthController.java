package com.example.smartseat.controller;

import com.example.smartseat.entity.ExamSchedule;
import com.example.smartseat.entity.Seating;
import com.example.smartseat.entity.User;
import com.example.smartseat.repository.ExamScheduleRepository;
import com.example.smartseat.repository.SeatingRepository;
import com.example.smartseat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private SeatingRepository seatingRepository;
    @Autowired private ExamScheduleRepository examScheduleRepository;

    // ── Admin Register ──────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");
        String role     = body.getOrDefault("role", "ADMIN");

        if (username == null || username.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Username is required"));

        if (password == null || password.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Password is required"));

        if (userRepository.findByUsername(username.trim().toLowerCase()).isPresent())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Username already taken"));

        User user = new User();
        user.setUsername(username.trim().toLowerCase());
        user.setPassword(password);
        user.setRole(role.toUpperCase());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Account created successfully",
            "username", username.trim(),
            "role", role.toUpperCase()
        ));
    }

    // ── Student Register ────────────────────────────────────────────────────
    @PostMapping("/register/student")
    public ResponseEntity<Map<String, Object>> registerStudent(
            @RequestBody Map<String, String> body) {

        String username  = body.get("username");
        String password  = body.get("password");
        String studentId = body.get("studentId");
        String fullName  = body.get("fullName");

        if (username == null || username.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Username is required"));

        if (password == null || password.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Password is required"));

        if (studentId == null || studentId.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Student ID is required (e.g. A001)"));

        if (userRepository.findByUsername(username.trim().toLowerCase()).isPresent())
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Username already taken"));

        User user = new User();
        user.setUsername(username.trim().toLowerCase());
        user.setPassword(password);
        user.setRole("STUDENT");
        user.setStudentId(studentId.trim().toUpperCase());
        user.setFullName(fullName != null ? fullName.trim() : "");
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Student account created successfully",
            "username", username.trim(),
            "role", "STUDENT"
        ));
    }

    // ── Login ───────────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        Optional<User> found = userRepository
            .findByUsername(username.trim().toLowerCase());

        if (found.isEmpty())
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Account not found. Please register first."));

        if (!found.get().getPassword().equals(password))
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Incorrect password."));

        User user = found.get();

        // ── Student Login ───────────────────────────────────────────────────
        if ("STUDENT".equals(user.getRole())) {

            // Only check if seating has been generated
            List<Seating> allSeatings = seatingRepository.findAll();
            if (allSeatings.isEmpty()) {
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Seating has not been generated yet. " +
                               "Please contact admin."));
            }

            // Find this student's seat
            Seating mySeat = allSeatings.stream()
                .filter(s -> s.getStudentId()
                    .equalsIgnoreCase(user.getStudentId()))
                .findFirst().orElse(null);

            if (mySeat == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "No seat found for Student ID: " +
                               user.getStudentId() +
                               ". Please contact admin."));
            }

            // Get exam info for display (optional)
            Optional<ExamSchedule> exam =
                examScheduleRepository.findByActiveTrue();

            return ResponseEntity.ok(Map.of(
                "success",    true,
                "role",       "STUDENT",
                "username",   user.getUsername(),
                "fullName",   user.getFullName() != null ? user.getFullName() : "",
                "studentId",  user.getStudentId(),
                "hallId",     mySeat.getHallId(),
                "seatNumber", mySeat.getSeatNumber(),
                "examName",   exam.isPresent()
                    ? exam.get().getExamName() : "Upcoming Exam",
                "examTime",   exam.isPresent()
                    ? exam.get().getExamDateTime().toString() : ""
            ));
        }

        // ── Admin Login ─────────────────────────────────────────────────────
        return ResponseEntity.ok(Map.of(
            "success",  true,
            "role",     "ADMIN",
            "username", user.getUsername()
        ));
    }

    // ── Get student's seat ──────────────────────────────────────────────────
    @GetMapping("/my-seat/{studentId}")
    public ResponseEntity<?> getMySeat(@PathVariable String studentId) {
        return seatingRepository.findAll().stream()
            .filter(s -> s.getStudentId().equalsIgnoreCase(studentId))
            .findFirst()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(404).body(
                Map.of("message", "No seat found for: " + studentId)));
    }

    // ── Get all users ───────────────────────────────────────────────────────
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}