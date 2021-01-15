package com.apos.workflow.remote;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apos.plugins.PluginSocketLoader;
import com.apos.socket.ClientStub;

public class WorkflowStub extends ClientStub{

	private static final String START_WORKFLOW = "startWorkflow";
	private String contextWf;
	private static Logger logger = LoggerFactory.getLogger(WorkflowStub.class);

	public WorkflowStub(String host, int port) {
		super(host, port);
	}

	public void initContextWf(){
		
		if(this.contextWf==null || this.contextWf.trim().isEmpty()) {
			contextWf  = runCommand("initContext",Arrays.asList());
			logger.debug(contextWf);
		}
		
	}
	public void startWorkflow(String contextId, String wfResourceId, HashMap<String, String> params) {	
		initContextWf();
		runCommand(START_WORKFLOW, Arrays.asList(contextWf, new JSONObject(params).toString()));
	}

	
}
