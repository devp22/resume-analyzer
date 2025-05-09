package com.example.resume_analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Slf4j
@RequestMapping("/api")
public class ResumeController {

    @Autowired
    private FileService fileService;

    @GetMapping("/hello")
    public String greet(){
        return "Hello there!";
    }

    @PostMapping("/file")
    public ResponseEntity<String> file(@RequestBody String fileName){
        System.out.println("hello");
        System.out.println(fileName);
        return ResponseEntity.ok(fileName);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("got file: " + file.getOriginalFilename());
        return new ResponseEntity<>(fileService.uploadFile(file), HttpStatus.OK);
    }


}