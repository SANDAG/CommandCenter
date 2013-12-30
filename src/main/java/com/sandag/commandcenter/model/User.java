package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authenticated_user") // user is an SQL reserved word
public class User
{

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "principal", unique = true)
    private String principal;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getPrincipal()
    {
        return principal;
    }

    public void setPrincipal(String principal)
    {
        this.principal = principal;
    }

    public boolean isSame(User another)
    {
        return id != null && id.equals(another.getId());
    }
    
}
