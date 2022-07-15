package com.apos.workflow.runtime;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowAgent {

	@Autowired
	WorkflowMonitorContext monitorCtx;
	 public String startWorkflow(String contextId, String xpdl, String jobTicketData,Map<String,String> mapParameters) throws Exception {
		    WorkflowContextInterface context = getContext(contextId);
		    context.startWorkflow( xpdl, jobTicketData,mapParameters);
		    return contextId;
		  }
	 
	 private WorkflowContextInterface getContext(String contextId) throws Exception {
		    WorkflowContextInterface context = monitorCtx.getContext(contextId);
		    if (context == null) { throw new Exception("Invalid workflow context :" + contextId); }
		    return context;
		  }
	 
	 public static void main(String[] args) throws Exception {
		 WorkflowAgent agent = new WorkflowAgent();
		 String xpdl="D:\\sefas\\master\\apos\\src\\main\\resources\\47";
		String jobTicketData="C:/hcs/env/test/client_4.1/tagTest2_1.1/config/1142.prop";
		Map<String, String> mapParameters = new HashMap<String, String>();
		agent.startWorkflow("1", xpdl, jobTicketData, mapParameters );
	}
}
