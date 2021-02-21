package com.apos.workflow.runtime;

import java.util.Map;

public interface WorkflowContextInterface {

	  public String startWorkflow(String xpdl, String jobTicketData,Map<String,String> mapParameters) throws Exception;

}
