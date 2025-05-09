package com.example.resume_analyzer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/api")
public class ResumeController {

    private FileService fileService;

    @GetMapping("/hello")
    public String greet(){
        return "Hello there!";
    }

    @GetMapping("/file")
    public ResponseEntity<String> file(@RequestBody String fileName){
        return ResponseEntity.ok(fileName);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(MultipartFile file){
        return new ResponseEntity<>(fileService.uploadFile(file),HttpStatus.OK);

    }

}