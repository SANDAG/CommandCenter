package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job")
public class Job
{

    public enum Status
    {
        QUEUED, RUNNING, COMPLETE, ARCHIVED, DELETED;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "model")
    private String modelName;

    private Status status;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    public Status getStatus()
    {
        return status;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getModelName()
    {
        return modelName;
    }

    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    };

}
