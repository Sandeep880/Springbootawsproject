package com.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EC2Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instanceId;
    private String instanceType;

    public Long getId() {
        return id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }
}
