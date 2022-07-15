package com.apos.workflow.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apos.rest.controllers.websocket.AposSocketHandler;

@Component
public class WorkflowMonitor implements JobTicketListener{

	@Autowired
	AposSocketHandler socketHandler;
	
	private WorkflowMonitor() {
		socketHandler = new AposSocketHandler();
	}
	
	@Override
	public void runtimeChange(Object obj) {
		
		
	}

	public static JobTicketListener getMonitor() {
		return null;
	}

}
