package com.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.JobStatus;

public interface JobStatusRepository extends JpaRepository<JobStatus, Long> {
	
	 JobStatus findByJobId(String jobId);

}
