package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_sequence")
public class Sequence
{

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;
    
    @Column(name = "value")
    private Integer value;

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }
    
}
