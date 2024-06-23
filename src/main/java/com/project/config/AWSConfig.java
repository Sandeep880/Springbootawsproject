package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {
	
	
	private String accessKey ="AKIAX5XSI5UT4M674AFL";
	
	
	private String accessSecretKey ="SEUYFy4LLDLvDG184lP2qM+PE/NFmi5MUCH3Wf7I";
	
	
	private String region = "ap-south-1";
	

	
	
	@Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AKIAX5XSI5UT4M674AFL", "SEUYFy4LLDLvDG184lP2qM+PE/NFmi5MUCH3Wf7I");
        return S3Client.builder()
                .region(Region.AP_SOUTH_1)  
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    @Bean
    public Ec2Client ec2Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AKIAX5XSI5UT4M674AFL", "SEUYFy4LLDLvDG184lP2qM+PE/NFmi5MUCH3Wf7I");
        return Ec2Client.builder()
                .region(Region.AP_SOUTH_1)  
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

}
