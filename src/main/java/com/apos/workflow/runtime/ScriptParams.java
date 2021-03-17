package com.apos.workflow.runtime;

public class ScriptParams {
	
	public static final String STRING = "string";
	public static final String VALUE = "value";
	public static final String TYPE = "type";
	private String type;
	private String value;
	private String name;

	ScriptParams(String name, String value, String type){
		this.name =name;
		this.value =value;
		this.type =type;
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
	
	
}
