package com.apos.utils;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	 public static HashMap<String, String> fromJsonHashMap(String name, String inHashMap) throws JSONException {
		    JSONObject obj = new JSONObject(inHashMap);
		    JSONObject map = obj.getJSONObject(name);
		    return fromJsonHashMap(map);
		  }
	
	@SuppressWarnings("unchecked")
	  public static HashMap<String, String> fromJsonHashMap(JSONObject map) throws JSONException {
	    HashMap<String, String> returned = new HashMap<String, String>();

	    Iterator<String> it = map.keys();
	    while (it.hasNext()) {
	      String curK = it.next();
	      String curVal = map.getString(curK);
	      returned.put(curK, curVal);
	    }
	    return returned;
	  }

}
