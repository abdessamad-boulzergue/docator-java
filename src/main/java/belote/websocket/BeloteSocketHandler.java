package belote.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class BeloteSocketHandler extends TextWebSocketHandler {
	private Logger logger = Logger.getLogger(BeloteSocketHandler.class);
	private HashMap<String,CopyOnWriteArrayList<WebSocketSession>> topics = new HashMap<>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		
	}
	
	
	public void broadcast(String topic, JSONObject data) {
		 CopyOnWriteArrayList<WebSocketSession> sessions = topics.get(topic);
		 if(sessions!=null && sessions.size()>0) {
			try {
				 for(WebSocketSession session : sessions) {
					 if(session.isOpen())
					    session.sendMessage(new TextMessage(data.toString()));
				 }
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		 }
		 
	}
	

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// traitement lors de la connection d'un nouveau user
		super.afterConnectionEstablished(session);
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