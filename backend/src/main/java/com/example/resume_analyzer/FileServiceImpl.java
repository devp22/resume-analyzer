package com.example.resume_analyzer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import jakarta.annotation.PostConstruct;

public class FileServiceImpl implements FileService{
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.accessKeyId}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    private AmazonS3 s3Client;

    @PostConstruct
    private void initialize() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile multipartFile) {
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String filePath = "";
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());
            filePath = todayDate+"/"+multipartFile.getOriginalFilename();
            s3Client.putObject(bucketName, filePath, multipartFile.getInputStream(), objectMetadata);
            fileUploadResponse.setFilePath(filePath);
            fileUploadResponse.setDateTime(LocalDateTime.now());
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return fileUploadResponse;
    }
}
