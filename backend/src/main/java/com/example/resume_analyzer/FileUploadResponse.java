package com.example.resume_analyzer;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FileUploadResponse {
    private String filePath;
    private LocalDateTime dateTime;
}
