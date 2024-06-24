package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.entity.JobStatus;
import com.project.repo.JobStatusRepository;
import com.project.entity.JobStatus;
import com.project.service.AwsService;


@RestController
@RequestMapping("/api")
public class AwsController {
	


       @Autowired
       private AwsService awsService;
       
       @Autowired
       private JobStatusRepository jobStatusRepository;

        @PostMapping("/discoverServices")
        public String discoverServices(@RequestBody List<String> services) {
            return awsService.discoverServices(services);
        }
        
        @GetMapping("/getJobResult/{jobId}")
        public String getJobResult(@PathVariable String jobId) {
            JobStatus jobStatus = jobStatusRepository.findByJobId(jobId);
            if (jobStatus != null) {
                return jobStatus.getStatus();
            } else {
                return "Job ID not found";
            }
        }
        
        @GetMapping("/getDiscoveryResult/{service}")
        public List<String> getDiscoveryResult(@PathVariable String service) {
        	
        	if("S3".equalsIgnoreCase(service))
        	{
        		return awsService.getS3Bucket();
        	}
        	else if ("EC2".equalsIgnoreCase(service)) {
                return awsService.getInstanceIds();
            } else {
                throw new IllegalArgumentException("Service not supported");
            }
        }
        
        @PostMapping("/getS3BucketObjects/{bucketName}")
        public String getS3BucketObjects(@PathVariable String bucketName) {
            return awsService.discoverS3BucketObjects(bucketName);
        }
        
        @GetMapping("/getS3BucketObjectCount/{bucketName}")
        public Long getS3BucketObjectCount(@PathVariable String bucketName) {
            return awsService.getS3BucketObjectCount(bucketName);
        }
        
        @GetMapping("/getS3BucketObjectLike/{bucketName}/{pattern}")
        public List<String> getS3BucketObjectLike(@PathVariable String bucketName, @PathVariable String pattern) {
            return awsService.getS3BucketObjectLike(bucketName, pattern);
        }
        
        

}
