package com.apos.rest.controllers.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class AposWebSocketConfig implements WebSocketConfigurer {
    
	@Autowired
	AposSocketHandler handler;
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		HandshakeInterceptor interceptors = new AposHandshakeInterceptor();
		registry.addHandler(handler, "/aposWebSocket")
		.setAllowedOrigins("*")
		.addInterceptors(interceptors );
	}

}