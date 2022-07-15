package com.apos.rest.controllers.websocket.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

//@Controller
public class GreetingController {

	 private SimpMessagingTemplate template;

	    @Autowired
	    public GreetingController(SimpMessagingTemplate template) {
	        this.template = template;
	    }
	
    @MessageMapping("/greetings")
    public String handle(String greeting) {
        return "[" + System.currentTimeMillis() + ": " + greeting;
    }

}