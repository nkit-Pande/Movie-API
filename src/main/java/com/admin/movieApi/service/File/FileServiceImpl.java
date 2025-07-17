package com.admin.movieApi.service.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.*;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private S3Client s3Client;

    @Value("${app.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String fileKey = (path.endsWith("/") ? path : path + "/") + fileName;
            
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build(),
                RequestBody.fromBytes(file.getBytes())
            );
            
            // Return the full S3 URL
            return String.format("https://%s.s3.%s.amazonaws.com/%s", 
                bucketName, 
                region, 
                fileKey);
        } catch (S3Exception e) {
            throw new IOException("Error uploading file to S3: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        try {
            String fileKey = (path.endsWith("/") ? path : path + "/") + fileName;
            return s3Client.getObject(
                GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build()
            );
        } catch (S3Exception e) {
            throw new FileNotFoundException("File not found in S3: " + e.getMessage());
        }
    }
}
