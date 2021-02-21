package com.apos.workflow.runtime;

import java.util.Map;

public interface WorkflowJobTicketInterface {

	void setJobTicketDataFilePath(String ticketDataPath);

	void addJobTicketParams(Map<String, String> params);

	void setXpdl(String absolutePath);

	void initJobTicket(String id);

	void setMonitorRunningContext(WorkflowMonitorContext workflowMonitorContext);

	String start() throws Exception;

}
