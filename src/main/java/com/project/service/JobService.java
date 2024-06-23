package com.project.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class JobService {
	
	private final Map<String, JobStatus> jobs = new ConcurrentHashMap<>();
    private final Map<String, List<String>> discoveryResults = new ConcurrentHashMap<>();

    public String createJob() {
        String jobId = "job-" + System.currentTimeMillis();
        jobs.put(jobId, JobStatus.IN_PROGRESS);
        return jobId;
    }

    public void updateJobWithResults(String jobId, String service, List<String> results) {
        discoveryResults.put(service, results);
        jobs.put(jobId, JobStatus.SUCCESS);
        
        System.out.println(jobs);
        System.out.println(discoveryResults);
    }

    public String getJobStatus(String jobId) {
        return jobs.getOrDefault(jobId, JobStatus.FAILED).name();
    }

    public List<String> getDiscoveryResult(String service) {
        return discoveryResults.getOrDefault(service, List.of());
    }

    public int getS3BucketObjectCount(String bucketName) {
        List<String> objects = discoveryResults.get("S3Objects");
        if (objects == null) {
            return 0;
        }
        return (int) objects.stream().filter(obj -> obj.startsWith(bucketName)).count();
    }

    public List<String> getS3BucketObjectsLike(String bucketName, String pattern) {
        List<String> objects = discoveryResults.get("S3Objects");
        if (objects == null) {
            return List.of();
        }
        return objects.stream()
                .filter(obj -> obj.startsWith(bucketName) && obj.contains(pattern))
                .collect(Collectors.toList());
    }

    enum JobStatus {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }

}
