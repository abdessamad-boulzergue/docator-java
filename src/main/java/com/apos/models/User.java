package com.apos.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	public User() {
	}
	@Id
	
	private long id;
	private String name;
	private String description;
	private String password;
	private String mail;
	private String phone;
	private String CONTENT;
	
	public  long getId() {
		return id;
	}
	public  String getName() {
		return name;
	}
	public  String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id+" , "+this.name;
	}
}
