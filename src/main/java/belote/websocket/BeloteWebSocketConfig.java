package belote.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class BeloteWebSocketConfig implements WebSocketConfigurer {
    
	@Autowired
	BeloteSocketHandler handler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		HandshakeInterceptor interceptors = new BeloteHandshakeInterceptor();
		
		registry.addHandler(handler, "/beloteWebSocket")
		
		.setAllowedOrigins("*")
		.addInterceptors(interceptors );
		
	}

}