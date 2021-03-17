package com.apos.rest.controllers.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class AposWebSocketConfig implements WebSocketConfigurer {


	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		HandshakeInterceptor interceptors = new AposHandshakeInterceptor();
		registry.addHandler(new AposSocketHandler(), "/aposWebSocket")
		.setAllowedOrigins("*")
		.addInterceptors(interceptors );
	}

}