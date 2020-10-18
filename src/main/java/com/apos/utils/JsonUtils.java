package com.apos.utils;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	 public static HashMap<String, JSONObject> fromJsonHashMap(String name, String inHashMap)  {
		 HashMap<String, JSONObject> returned = new HashMap<String, JSONObject>();   
		 try {
		    	JSONObject obj = new JSONObject(inHashMap);
			    JSONObject map = obj.getJSONObject(name);
			    returned= fromJsonHashMap(map);
			} catch (JSONException e) {
				JsonUtils.logger.error(e.toString());
			}
			return returned;
		  }
	
	@SuppressWarnings("unchecked")
	  public static HashMap<String, JSONObject> fromJsonHashMap(JSONObject map) throws JSONException {
	    HashMap<String, JSONObject> returned = new HashMap<>();

	    Iterator<String> it = map.keys();
	    while (it.hasNext()) {
	      String curK = it.next();
	      JSONObject curVal = map.getJSONObject(curK);
	      returned.put(curK, curVal);
	    }
	    return returned;
	  }

}
