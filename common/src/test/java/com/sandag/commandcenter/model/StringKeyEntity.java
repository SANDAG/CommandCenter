package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "string_key_entity")
public class StringKeyEntity
{
    @GeneratedValue
    @Column(name = "id")
    @Id
	public String id;
}
