package com.apos.rest.controllers.websocket;

import org.json.JSONObject;

public class AposWebSocketData {

	private String type;
	private String message;
	
	private AposWebSocketData(String type,String msg) {
		this.type = type;
		this.message=msg;
	}
	
	
	public String getType() {
		return type;
	}


	public String getMessage() {
		return message;
	}


	public static AposWebSocketData parse(String data){
		AposWebSocketData socketData = null;
		
			JSONObject dataObj = new JSONObject(data);
			if(dataObj.has("type") && dataObj.has("message")) {
				socketData = new AposWebSocketData(dataObj.getString("type"), dataObj.getString("message"));
			}else {
				throw new  IllegalArgumentException("data object must have type and message");
			}
		
		return socketData;
	}


	public static AposWebSocketData getData(String type, String msg) {
		return new AposWebSocketData(type, msg);
	}
}
