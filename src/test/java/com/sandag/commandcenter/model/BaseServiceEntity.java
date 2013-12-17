package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// defined here because the Spring/Hibernate configuration scans this package
@Entity
@Table(name = "base_service_test_entity")
public class BaseServiceEntity
{
    @GeneratedValue
    @Column(name = "id")
    @Id
    private Integer id;

    @Column(name = "string_value")
    private String value;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}