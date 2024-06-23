package com.project.repo;

import com.project.entity.EC2Instance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EC2InstanceRepository extends JpaRepository<EC2Instance , Long> {
}
