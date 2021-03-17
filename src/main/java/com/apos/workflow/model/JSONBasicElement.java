package com.apos.workflow.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONBasicElement {

	public static final int INDEX_TYPE = 0;
	public static final int INDEX_ATTRIBUTES = 1;
	public static final int INDEX_CHILDREN = 2;
	private  JSONArray basicResource;
	
	public JSONBasicElement(String title ,JSONObject attributtes, List<JSONArray> childs) {
		basicResource = new JSONArray();
		basicResource.put(title);
		basicResource.put(attributtes);
		JSONArray childsArray = new JSONArray();
		for(JSONArray child :  childs) {
			if(isBasicElement(child)) {
				childsArray.put(child);
			}else {
	            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
			}
		}
		basicResource.put(childsArray);
	}
	
	
	public static boolean isBasicElement(JSONArray resource ) {
		return 
                resource.length() == 3
                && resource.get(INDEX_TYPE) instanceof String
                && resource.get(INDEX_ATTRIBUTES) instanceof JSONObject
                && resource.get(INDEX_CHILDREN) instanceof JSONArray;
		
	}
}
