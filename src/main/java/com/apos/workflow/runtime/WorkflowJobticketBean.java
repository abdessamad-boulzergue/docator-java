package com.apos.workflow.runtime;

import java.util.HashMap;
import java.util.Map;

public class WorkflowJobticketBean implements WorkflowJobTicketInterface{

	private JobTicket _jobTicket = null;
	  private String    modelPath      = null;
	  private Map<String, String> _mapParameters= new HashMap<String,String>();
	private String _jobTicketDataFilePath;
	  protected String       _serializedJobTicketData;

	  
	@Override
	public void setJobTicketDataFilePath(String ticketDataPath) {
		_jobTicketDataFilePath = ticketDataPath;
	}

	@Override
	public void addJobTicketParams(Map<String, String> params) {
	    _mapParameters = params;

	}

	@Override
	public void setXpdl(String absolutePath) {
		
		modelPath = absolutePath;
	}

	@Override
	public void initJobTicket(String contextId) {
	    if (_jobTicketDataFilePath != null) {
	        _jobTicket = new JobTicket(modelPath, _jobTicketDataFilePath, contextId,_mapParameters);
	      } else {
	        _jobTicket = new JobTicket(modelPath, null, _serializedJobTicketData, contextId,_mapParameters);
	      }
	    }

	@Override
	public void setMonitorRunningContext(WorkflowMonitorContext workflowMonitorContext) {
		if (_jobTicket != null) {
		      _jobTicket.setMonitorRunningContext(workflowMonitorContext);
		    }		
	}

	@Override
	public String start() throws Exception {
		if(_jobTicket!=null) {
			_jobTicket.start();
	        return _jobTicket.getId();
		}
		 throw new Exception("Trying to start null jobticket");		
	}

}
