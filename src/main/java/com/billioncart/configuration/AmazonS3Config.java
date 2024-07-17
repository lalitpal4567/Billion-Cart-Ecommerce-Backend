package com.billioncart.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.partitions.model.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config{

//    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey = "AKIAXYKJSI77W5SX2AQQ";

//    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey = "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";

//    @Value("${cloud.aws.region.static}")
//    private String region = "US East (N. Virginia) us-east-1";
    Regions region = Regions.US_EAST_1;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}

