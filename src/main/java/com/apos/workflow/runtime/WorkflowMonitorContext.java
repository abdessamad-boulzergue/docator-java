package com.apos.workflow.runtime;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apos.rest.controllers.websocket.AposSocketHandler;
import com.apos.rest.exceptions.ResourceAccessException;
import com.apos.workflow.runtime.utils.WorkflowEventRuntimeBuilder;
@Component
public class WorkflowMonitorContext extends WorkflowCommonContext implements JobTicketListener{
	private Logger logger = Logger.getLogger(WorkflowMonitorContext.class);
	private String id="1";
	  private static WorkflowMonitorContext                        _instance                 = null;
	  private static HashMap<String, WorkflowCommonContext> _contexts                 = new HashMap<String, WorkflowCommonContext>();
	  private WorkflowJobTicketInterface runtimeInterface;
	  @Autowired
		AposSocketHandler socketHandler;
	  
	  WorkflowMonitorContext(){
		  String key="1";
		  runtimeInterface = new WorkflowJobticketBean();
		_contexts.put(key, this );
	  }
	public String startWorkflow(String modelPath,String ticketDataPath,Map<String,String> params) {
			File modelFile = new File(modelPath);
			if(modelFile.canRead()) {
				WorkflowJobTicketInterface jobTicketInterface = (WorkflowJobTicketInterface) runtimeInterface;
			      jobTicketInterface.setXpdl(modelFile.getAbsolutePath());
			      jobTicketInterface.setJobTicketDataFilePath(ticketDataPath);
			      jobTicketInterface.addJobTicketParams(params);
			      return initializeAndStartJobTicket(jobTicketInterface);
			}else {
				throw new  ResourceAccessException(modelPath);
			}
	}

	private String initializeAndStartJobTicket(WorkflowJobTicketInterface jobTicketInterface) {
		try {
		jobTicketInterface.initJobTicket(getId());
		jobTicketInterface.setMonitorRunningContext(this);
		jobTicketInterface.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
			WorkflowEventRuntimeBuilder builder = new WorkflowEventRuntimeBuilder();
			builder.setContext(getId());
			builder.append("exception", e.getMessage());
			socketHandler.broadcast("WORKFLOW_RUNTIME_EVENTS_"+builder.getContext(),builder.build());
		}
		return null;
	}

	private String getId() {
		return id;
	}

	public static WorkflowMonitorContext getInstancex() {
	    if (_instance == null) {
	        _instance = new WorkflowMonitorContext();
	      }
	      return _instance;
	    }

	public WorkflowContextInterface getContext(String contextId) {
	    synchronized (_contexts) {
	        return _contexts.get(contextId);
	      }
	    }
	@Override
	public void runtimeChange(Object obj) {
		WorkflowEventRuntimeBuilder builder = new WorkflowEventRuntimeBuilder((JSONObject) obj);
		socketHandler.broadcast("WORKFLOW_RUNTIME_EVENTS_"+builder.getContext(), (JSONObject) obj);
	}
}
