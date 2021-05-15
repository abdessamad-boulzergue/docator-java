package com.apos.rest.controllers.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AposSocketHandler extends TextWebSocketHandler {
	Logger logger = Logger.getLogger(AposSocketHandler.class);
	HashMap<String,CopyOnWriteArrayList<WebSocketSession>> topics = new HashMap<>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		AposWebSocketData data =null;
		try {
			 data = AposWebSocketData.parse(message.getPayload());

		} catch (Exception e) {
			data = AposWebSocketData.getExceptionData(e.getMessage());
		}
		if(data.isRegister()) {
		    register(data.getTopic(),session);
		    data.setSession(session.getId());
			broadcastResigter(data.getJsonData(),session);
		}
	}
	
	private void broadcastResigter(JSONObject jsonData, WebSocketSession session) throws IOException {
		broadcast("register", jsonData, session);
	}

	public void broadcast(String topic, JSONObject data) {
		AposWebSocketData dataObj =  AposWebSocketData.withJson(data);
		dataObj.setType(topic);
		 CopyOnWriteArrayList<WebSocketSession> sessions = topics.get(topic);
			try {
				 for(WebSocketSession session : sessions) {
					 if(session.isOpen())
					    session.sendMessage(new TextMessage(dataObj.toString()));
				 }
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		 
	}
	
	public void broadcast(String topic,JSONObject data,WebSocketSession session) throws IOException {
		AposWebSocketData dataObj =  AposWebSocketData.withJson(data);
		dataObj.setType(topic);
		session.sendMessage(new TextMessage(dataObj.toString()));
		 
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Iterator<String> keys = topics.keySet().iterator();
		while(keys.hasNext()) {
			topics.get(keys.next()).remove(session);
		 }
		super.afterConnectionClosed(session, status);
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		String error = exception.getMessage();
		error = error!=null? error : "error occurred";
		session.sendMessage(new TextMessage(error));
	}

	private void register(String topic, WebSocketSession session) {

		CopyOnWriteArrayList<WebSocketSession> sessions = topics.get(topic);
		 if(sessions==null) {
			 sessions = new CopyOnWriteArrayList<> ();
			 topics.put(topic, sessions);
		 }
		 if(!sessions.contains(session))
		    sessions.add(session);
	}
	

}