package com.apos.workflow.plugin;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.apos.plugins.EnginesScriptlet;
import com.apos.workflow.model.Activity;
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

	public void populate(JobTicketRunner jobTicketRunner, JobTicketData jobTicketData, Activity activity) throws Exception {
		populateRuntimeArgs(jobTicketRunner,jobTicketData);
		populateOutputArgs(jobTicketData);
		String nameId = activity.getName() + "_" + activity.getId();
		nameId = nameId.replace(' ', '_');
		updateScripletArgs(nameId,jobTicketData.getRunningEnvironment());
	}

	private void updateScripletArgs(String nameId, HashMap<String, String> params) throws Exception {
		Iterator<String> paramList = params.keySet().iterator();
	    while (paramList.hasNext()) {
	      String nextKey = paramList.next();
	      String scriptletName = nextKey;
	      int dotPos = nextKey.indexOf('.');
	      if (dotPos != -1) {
	        String stepName = nextKey.substring(0, dotPos);
	        if (nameId.equals(stepName)) {
	          scriptletName = nextKey.substring(dotPos + 1);
	        }
	      }
	      if (scriptletName != null) {
	        String nextValue = params.get(nextKey).toString();
	        if (this.containsScriptletKey(scriptletName)) {
	              this.setScriptletArgs(scriptletName, nextValue);
	        }
	      }
	    }
	}
}
