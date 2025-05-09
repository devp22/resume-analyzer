package com.example.resume_analyzer;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileUploadResponse uploadFile(MultipartFile multipartFile);
}
