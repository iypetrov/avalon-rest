package org.avalon.avalonrest.images;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class S3Service {
    @Value("${app.endpoint.baseUrl}")
    private String baseUrl;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String key, MultipartFile image) throws IOException, NoSuchAlgorithmException, FailedToUploadObjectToS3Exception {
        String fileMd5Hash = computeMd5Hash(image);
        if (isFileIdenticalInS3(key, fileMd5Hash)) {
            logger.warn("File already exists and is identical. Skipping upload.");
            return baseUrl + "/api/v0/images/" + key;
        }
        Path tempFile = Files.createTempFile(null, null);
        image.transferTo(tempFile.toFile());
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(image.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, tempFile);
            logger.info("File uploaded successfully to S3: {}", key);
        } catch (Exception ex) {
            logger.error("Failed to upload file to S3: {}", ex.getMessage());
            throw new FailedToUploadObjectToS3Exception(ex.getMessage());
        } finally {
            Files.deleteIfExists(tempFile);
        }
        return baseUrl + "/api/v0/images/" + key;
    }

    private boolean isFileIdenticalInS3(String keyName, String fileMd5Hash) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            String existingEtag = headObjectResponse.eTag();
            if (existingEtag != null) {
                existingEtag = existingEtag.replace("\"", "");
            }
            return existingEtag != null && existingEtag.equals(fileMd5Hash);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                logger.info("File not found in S3: {}", keyName);
                return false;
            } else {
                logger.error("Error checking file in S3: {} - {}", keyName, e.awsErrorDetails().errorMessage(), e);
                throw e;
            }
        }
    }

    private String computeMd5Hash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] fileBytes = file.getBytes();
        md.update(fileBytes);
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public byte[] getFile(UUID key, HttpServletResponse response) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(key.toString())
                .bucket(bucketName)
                .build();
        try (InputStream inputStream = s3Client.getObject(objectRequest)) {
            byte[] buffer = new byte[8192];  // 8 KB buffer
            int bytesRead;
            ServletOutputStream outputStream = response.getOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return new byte[0];
    }
}