package com.apos.workflow.remote;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WorkflowRemoteService implements WorkflowRemote{
	
	@Autowired
	@Qualifier("workflow_connection")
	JsonNode sourceConfig;
	
	
	WorkflowStub wfStub;
	WorkflowRemoteService(){
		
	}
	@PostConstruct
	public void init() {
		

		/*
		JsonNode src = sourceConfig.get("workflow_server");
		String host = src.get("address").asText();
		Integer port = Integer.valueOf(src.get("port").asText());
		wfStub = new WorkflowStub(host, port);
		*/
			
	}
	
	public void startWorkflow() {
		
		String contextId="";
		String wfResourceId="1";
		HashMap<String, String> params = new HashMap<>();
		wfStub.startSession();
		wfStub.startWorkflow(contextId, wfResourceId, params);
	}
}
