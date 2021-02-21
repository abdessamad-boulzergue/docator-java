package com.apos.workflow.plugin;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.apos.plugins.EnginesScriptlet;
import com.apos.workflow.runtime.IJobTicketRunner;
import com.apos.workflow.runtime.JobTicketData;
import com.apos.workflow.runtime.JobTicketRunner;

abstract public class WorkflowScriptlet extends EnginesScriptlet{

	  static Logger  logger        = Logger.getLogger(WorkflowScriptlet.class);
	private IJobTicketRunner runner;
	private JobTicketData ticketdata;
	  
	  void logInfo(String message){
		  logger.info(message);
	  }

	  void logDebug(String message){
		  logger.debug(message);
	  }
	  void logWarn(String message){
		  logger.warn(message);
	  }
	  void logFatal(String message){
		  logger.fatal(message);
	  }

	public static WorkflowScriptlet getScriplet(EnginesScriptlet scriptlet) {
		return (scriptlet instanceof WorkflowScriptlet)? (WorkflowScriptlet)scriptlet : null;
	}

	public void populateRuntimeArgs(IJobTicketRunner jobTicketRunner, JobTicketData jobTicketData) {
		runner = jobTicketRunner;
		ticketdata = jobTicketData;
	}

	public void populateOutputArgs(JobTicketData jobTicketData) {
	    HashMap<String, String> extraParameters = jobTicketData.getRunningEnvironment();
	}

	public void populate(JobTicketRunner jobTicketRunner, JobTicketData jobTicketData) {
		populateRuntimeArgs(jobTicketRunner,jobTicketData);
		populateOutputArgs(jobTicketData);
	}
}
