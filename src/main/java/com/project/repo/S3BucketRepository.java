package com.project.repo;

import com.project.entity.S3Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3BucketRepository extends JpaRepository<S3Bucket, Long> {
}
