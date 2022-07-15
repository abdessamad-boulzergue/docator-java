package com.apos.rest.controllers.websocket;

import org.json.JSONObject;

public class AposWebSocketData {

	JSONObject data = new JSONObject();
	private static final String TYPE="type";
	private static final String TOPIC="topic";
	private static final String MSG="message";
	private static final Object REGISTER = "register";
	private static final String SESSION = "session";
	private static final String EXCEPTION = "Exception";
	private AposWebSocketData(String type,String msg) {
		data.put(TYPE, type);
		data.put(MSG, msg);
	}
	
	private AposWebSocketData(JSONObject dataObj) {
		data = dataObj;
	}
	
	public String getType() {
		return data.getString(TYPE);
	}


	public String getMessage() {
		return data.getString(MSG);
	}


	public static AposWebSocketData parse(String data){
		AposWebSocketData socketData = null;
		
			JSONObject dataObj = new JSONObject(data);
			if(dataObj.has(TYPE) ) {
				socketData = new AposWebSocketData(dataObj);
			}else {
				throw new  IllegalArgumentException("data object must have type");
			}
		
		return socketData;
	}
     
	@Override
	public String toString() {
		return data.toString();
	}
	public static AposWebSocketData getData(String type, String msg) {
		return new AposWebSocketData(type, msg);
	}

	public boolean isRegister() {
		return getType().equals(REGISTER);
	}

	public void setSession(String session) {
		data.put(SESSION, session);
	}

	public JSONObject getJsonData() {
		return data;
	}

	public static AposWebSocketData getExceptionData(String msg) {
		return new AposWebSocketData(EXCEPTION, msg);
	}

	public static AposWebSocketData withJson(JSONObject data) {
		return new AposWebSocketData(data);
	}

	public void setType(String topic) {
		data.put(TYPE, topic);
	}

	public String getTopic() {
		return data.getString(TOPIC);
	}
}
