package com.apos.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
@Table(name = "users")
public class User {

	public User() {
	}
	public User(Long id, String username,String password) {
		this.id = id;
		this.name = username;
		this.password = password;
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

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	public String getEmail() {
		// TODO Auto-generated method stub
		return mail;
	}
	public Date getCreationDate() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getRole() {
		// TODO Auto-generated method stub
		return "ADMIN";
	}
	  @Override
		public String toString() {
			JSONObject obj = new JSONObject();
			obj.put("id", id)
			.put("name", name)
			.put("password", password);
			return obj.toString(1);
		}
}
