package com.apos.rest.controllers.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AposSocketHandler extends TextWebSocketHandler {
	
	HashMap<String,CopyOnWriteArrayList<WebSocketSession>> topics = new HashMap<>();
	static final String DEFAULT_TOPIC = "default";

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		AposWebSocketData data =null;
		try {
			 data = AposWebSocketData.parse(message.getPayload());

		} catch (Exception e) {
			data = AposWebSocketData.getData("Exception", e.getMessage());
		}
		if(data.getType().equals("register")) {
		    register(data.getMessage(),session);
		    JSONObject json = new JSONObject();
		    json.put("session", session.getId());
			broadcast("register",json,session);
		}
	}

	 public void broadcast( JSONObject data)  {
		 broadcast(DEFAULT_TOPIC, data);
	 }

	public void broadcast(String topic, JSONObject data) {
		data.put("type", topic);
		 CopyOnWriteArrayList<WebSocketSession> sessions = topics.get(topic);
			try {
				 for(WebSocketSession session : sessions) {
					 if(session.isOpen())
					    session.sendMessage(new TextMessage(data.toString()));
				 }
			} catch (IOException e) {
				e.printStackTrace();
			}
		 
	}
	public void broadcast(String topic,JSONObject data,WebSocketSession session) throws IOException {
		    data.put("type", topic);
			session.sendMessage(new TextMessage(data.toString()));
		 
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
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		register(DEFAULT_TOPIC,session); 
		 
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