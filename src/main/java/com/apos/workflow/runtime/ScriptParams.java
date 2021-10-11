package com.apos.workflow.runtime;

public class ScriptParams {
	
	public static final String STRING = "string";
	public static final String VALUE = "value";
	public static final String TYPE = "type";
	public static final String REQUIRED = "required";
	private String type;
	private String value;
	private String name;
	private String required;

	ScriptParams(String name, String value, String type){
		this.name =name;
		this.value =value;
		this.type =type;
		this.required ="false";
	}
	
	ScriptParams(String name, String value, String type,String required){
		this.name =name;
		this.value =value;
		this.type =type;
		this.required =required;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	public String getRequired() {
		return required;
	}
	
	
}
