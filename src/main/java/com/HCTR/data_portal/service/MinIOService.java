package com.HCTR.data_portal.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinIOService {
    @Value("${minioClient.endpoint}")
    private String endpoint;
    @Value("${minioClient.credentials.accessKey}")
    private String accessKey;
    @Value("${minioClient.credentials.secretKey}")
    private String secretKey;
    String bucket = "dataportal";

    public void uploadMinIO(MultipartFile mapImage, MultipartFile timeSeries, String rootPath) throws IOException, NoSuchAlgorithmException, InvalidKeyException{
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpoint)
                            .credentials(accessKey, secretKey)
                            .build();
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(rootPath + '/' + mapImage.getOriginalFilename()).stream(
                                    mapImage.getInputStream(), mapImage.getSize(), -1)
                            .contentType(mapImage.getContentType())
                            .build());
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(rootPath + '/' + timeSeries.getOriginalFilename()).stream(
                                    timeSeries.getInputStream(), timeSeries.getSize(), -1)
                            .contentType(timeSeries.getContentType())
                            .build());

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}
