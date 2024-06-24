package com.project.service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.project.entity.EC2Instance;
import com.project.entity.JobStatus;
import com.project.entity.S3Bucket;
import com.project.entity.S3Object;
import com.project.repo.EC2InstanceRepository;
import com.project.repo.JobStatusRepository;
import com.project.repo.S3BucketRepository;
import com.project.repo.S3ObjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.Bucket;

@Service
public class AwsService {

   @Autowired
   private S3Client s3Client;
   
   @Autowired
   private Ec2Client ec2Client;
   @Autowired
   private EC2InstanceRepository ec2InstanceRepository;
   @Autowired
   private S3BucketRepository s3BucketRepository;
   
   
    public AwsService(S3Client s3Client, Ec2Client ec2Client) {
        this.s3Client = s3Client;
        this.ec2Client = ec2Client;
    }
    
    @Autowired
    private JobStatusRepository jobStatusRepository;
    
    @Autowired
    private S3ObjectRepository s3ObjectRepository;
    


    @Async
    public void discoverEC2Instances(String jobId) {

        try {
    	
        DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
        DescribeInstancesResponse response = ec2Client.describeInstances(request);

        for (Instance instance : response.reservations().stream().flatMap(r -> r.instances().stream()).toList()) {
            EC2Instance ec2Instance = new EC2Instance();
            ec2Instance.setInstanceId(instance.instanceId());
            System.out.println(instance);
            ec2Instance.setInstanceType(instance.instanceType().toString());
            
            ec2InstanceRepository.save(ec2Instance);
            
            }
        updateJobStatus(jobId, "Success");
        
        }
        catch(Exception e)
        {
        	updateJobStatus(jobId, "Failed");
        }
    }

    @Async
    public void discoverS3Buckets(String jobId) {
    
    try {	

        ListBucketsRequest request = ListBucketsRequest.builder().build();
        ListBucketsResponse response = s3Client.listBuckets(request);

        for (Bucket bucket : response.buckets()) {
            S3Bucket s3Bucket = new S3Bucket();
            System.out.println(bucket);
            s3Bucket.setBucketName(bucket.name());
            
            s3BucketRepository.save(s3Bucket);
        }
        updateJobStatus(jobId, "Success");
    }
    catch(Exception e)
    {
    	updateJobStatus(jobId, "Success");
    }
    }

    public String discoverServices(List<String> services) {
        String jobId = UUID.randomUUID().toString();
        createJobStatus(jobId, "In Progress");
        
        if (services.contains("EC2")) {
            discoverEC2Instances(jobId);
        }
        if (services.contains("S3")) {
            discoverS3Buckets(jobId);
        }
        return jobId;
    }
    
    
    private void createJobStatus(String jobId, String status) {
        JobStatus jobStatus = new JobStatus(jobId, status);
        jobStatusRepository.save(jobStatus);
    }

    private void updateJobStatus(String jobId, String status) {
        JobStatus jobStatus = jobStatusRepository.findByJobId(jobId);
        jobStatus.setStatus(status);
        jobStatusRepository.save(jobStatus);
    }
    
    public List<String> getS3Bucket()
    {
    	return s3BucketRepository.findAll()
    			                 .stream()
    			                 .map(S3Bucket :: getBucketName)
    			                 .collect(Collectors.toList());
    }
    
    public List<String> getInstanceIds()
    {
    	return ec2InstanceRepository.findAll()
    			                    .stream()
    			                    .map(EC2Instance :: getInstanceId)
    			                    .collect(Collectors.toList());
    }
    
    
    
    public List<String> getS3BucketObjects(String bucketName) {
        return s3ObjectRepository.findByBucketName(bucketName).stream()
                .map(S3Object::getObjectKey)
                .collect(Collectors.toList());
    }

    public String discoverS3BucketObjects(String bucketName) {
        String jobId = UUID.randomUUID().toString();
        createJobStatus(jobId, "In Progress");
        discoverS3BucketObjects(bucketName, jobId);
        return jobId;
    }
    
    
    @Async
    public void discoverS3BucketObjects(String bucketName, String jobId) {
        try {
            

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response response;
            do {
                response = s3Client.listObjectsV2(request);
                response.contents().forEach(s3Object -> {
                    S3Object object = new S3Object(bucketName, s3Object.key());
                    s3ObjectRepository.save(object);
                });
                request = request.toBuilder().continuationToken(response.nextContinuationToken()).build();
            } while (response.isTruncated());

            updateJobStatus(jobId, "Success");
        } catch (Exception e) {
            updateJobStatus(jobId, "Failed");
        }
    }
    
    
    public Long getS3BucketObjectCount(String bucketName) {
        return s3ObjectRepository.countByBucketName(bucketName);
    }
    
    public List<String> getS3BucketObjectLike(String bucketName, String pattern) {
        List<S3Object> s3Objects = s3ObjectRepository.findByBucketNameAndObjectKeyContaining(bucketName, pattern);
        
         return s3Objects.stream()
                 .map(S3Object :: getObjectKey )
                 .collect(Collectors.toList());
    }
    
    

}
