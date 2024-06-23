package com.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class S3Object {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String bucketName;
	    private String objectKey;

	    // Constructors, getters, and setters
	    public S3Object() {}

	    public S3Object(String bucketName, String objectKey) {
	        this.bucketName = bucketName;
	        this.objectKey = objectKey;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getBucketName() {
	        return bucketName;
	    }

	    public void setBucketName(String bucketName) {
	        this.bucketName = bucketName;
	    }

	    public String getObjectKey() {
	        return objectKey;
	    }

	    public void setObjectKey(String objectKey) {
	        this.objectKey = objectKey;
	    }

}
