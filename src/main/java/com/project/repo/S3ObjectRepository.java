package com.project.repo;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.S3Bucket;
import com.project.entity.S3Object;

public interface S3ObjectRepository extends JpaRepository<S3Object, Long> {
	
	
	List<S3Object> findByBucketName(String bucketName);
	
	Long countByBucketName(String bucketName);
	
	List<S3Object> findByBucketNameAndObjectKeyContaining(String bucketName, String pattern);

}
