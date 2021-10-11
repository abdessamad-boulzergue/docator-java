package com.apos.models;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="resource_type")
public class ResourceType {
	
	public static final String TYPE_WORKFLOW="workflow";
	public static final String TYPE_PLUGIN="plugin";
	public static final String TYPE_DOCUMENT = "document";
	public static final String TYPE_APPLICATION = "application";

	public ResourceType(){
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@Column(unique = true)
	private String name;

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
        if(Arrays.asList(TYPE_WORKFLOW, TYPE_PLUGIN,TYPE_DOCUMENT,TYPE_APPLICATION).contains(name)) {
   		 this.name = name;

		}else {
			throw new IllegalArgumentException(" unkown resource type  :  "+name);
		}

	}



	public Long getId() {
		return id;
	}


	public boolean hasName() {
		
		return name!=null && !name.trim().isEmpty();
	}
	
	@Override
	public String toString() {
		return "{"
				+ "name: "+ this.name+
	         "}";
	}

}
