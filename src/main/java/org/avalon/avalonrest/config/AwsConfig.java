package org.avalon.avalonrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AwsConfig {
    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    @Profile("local")
    public S3Client s3LocalClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                accessKeyId,
                secretAccessKey
        );
        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create("http://localhost:9000"))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    @Profile("prod")
    public S3Client s3ProdClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                accessKeyId,
                secretAccessKey
        );
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}