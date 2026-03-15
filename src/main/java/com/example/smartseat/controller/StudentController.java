package com.example.smartseat.controller;

import com.example.smartseat.entity.Student;
import com.example.smartseat.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // GET all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // POST add single student  { "name": "Alice" }
    @PostMapping
    public Student addStudent(@RequestBody Map<String, String> body) {
        Student student = new Student();
        student.setName(body.get("name").trim());
        return studentRepository.save(student);
    }

    // POST batch insert from AI assistant  { "names": ["Alice", "Bob"] }
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> addStudentsBatch(
            @RequestBody Map<String, Object> body) {

        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) body.get("names");

        if (names == null || names.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "No names provided",
                "inserted", 0
            ));
        }

        int count = 0;
        for (String name : names) {
            String trimmed = name.trim();
            if (!trimmed.isEmpty()) {
                Student s = new Student();
                s.setName(trimmed);
                studentRepository.save(s);
                count++;
            }
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Inserted " + count + " students successfully",
            "inserted", count
        ));
    }

    // DELETE student by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
