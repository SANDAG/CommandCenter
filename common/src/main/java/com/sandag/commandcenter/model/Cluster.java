package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cluster")
public class Cluster extends BaseEntity
{
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "active")
    private boolean active;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    // lower case for unique constraint
    public void setName(String name)
    {
        this.name = name.toLowerCase();
    }

    public boolean getActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

}
