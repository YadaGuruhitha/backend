package com.example.smartseat.service;

import com.example.smartseat.entity.Student;
import com.example.smartseat.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceService {

    @Value("${huggingface.api.url}")
    private String apiUrl;

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Autowired
    private StudentRepository studentRepository;

    public String extractNames(String text) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("inputs", "Extract student names from this text: " + text);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(apiUrl, request, String.class);

        String aiResponse = response.getBody();
        String[] names = aiResponse.split("\n");

        for (String name : names) {
            if (!name.trim().isEmpty()) {
                Student student = new Student();
                student.setName(name.trim());
                studentRepository.save(student);
            }
        }

        return "Students inserted successfully";
    }
}
