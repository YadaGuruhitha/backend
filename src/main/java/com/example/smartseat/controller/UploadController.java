package com.example.smartseat.controller;

import com.example.smartseat.service.HuggingFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
public class UploadController {

    @Autowired
    private HuggingFaceService huggingFaceService;

    @PostMapping("/uploadPDF")
    public String uploadPDF(@RequestParam("file") MultipartFile file) throws Exception {
        String text = new String(file.getBytes());
        return huggingFaceService.extractNames(text);
    }
}
