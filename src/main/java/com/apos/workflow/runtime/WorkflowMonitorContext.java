package com.apos.workflow.runtime;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.apos.rest.exceptions.ResourceAccessException;
public class WorkflowMonitorContext extends WorkflowCommonContext{

	private String id="1";
	  private static WorkflowMonitorContext                        _instance                 = null;
	  private static HashMap<String, WorkflowCommonContext> _contexts                 = new HashMap<String, WorkflowCommonContext>();
	  private WorkflowJobTicketInterface runtimeInterface;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String getId() {
		return id;
	}

	public static WorkflowMonitorContext getInstance() {
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
}
