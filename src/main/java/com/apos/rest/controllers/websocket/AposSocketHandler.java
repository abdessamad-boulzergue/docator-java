package com.apos.rest.controllers.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

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
		
		broadcast(data.getMessage());
	}

	 public void broadcast( String message) throws IOException {
		 broadcast(DEFAULT_TOPIC, message);
	 }

	public void broadcast(String topic, String message) throws IOException {
		 CopyOnWriteArrayList<WebSocketSession> sessions = topics.get(topic);
		 for(WebSocketSession session : sessions) {
			session.sendMessage(new TextMessage(message));
		 }
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
		session.sendMessage(new TextMessage(exception.getMessage()));
	}
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		 CopyOnWriteArrayList<WebSocketSession> sessions = topics.get(DEFAULT_TOPIC);
		 if(sessions==null) {
			 sessions = new CopyOnWriteArrayList<> ();
			 topics.put(DEFAULT_TOPIC, sessions);
		 }
		 
		 sessions.add(session);
	}

}