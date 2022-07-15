package com.apos.workflow.plugin;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.apos.plugins.EnginesScriptlet;
import com.apos.workflow.runtime.IJobTicketRunner;
import com.apos.workflow.runtime.JobTicketData;

abstract public class WorkflowScriptlet extends EnginesScriptlet{

	  static Logger  logger        = Logger.getLogger(WorkflowScriptlet.class);
	private IJobTicketRunner runner;
	private JobTicketData ticketdata;
	  private static String _OUTPUT_="OUTPUT.";

	  protected void logInfo(String message){
		  logger.info(message);
	  }

	  protected void logDebug(String message){
		  logger.debug(message);
	  }
	  protected void logWarn(String message){
		  logger.warn(message);
	  }
	  protected void logFatal(String message){
		  logger.fatal(message);
	  }

	public static WorkflowScriptlet getScriplet(EnginesScriptlet scriptlet) {
		return (scriptlet instanceof WorkflowScriptlet)? (WorkflowScriptlet)scriptlet : null;
	}

	public void populateRuntimeArgs(IJobTicketRunner jobTicketRunner, JobTicketData jobTicketData) {
		runner = jobTicketRunner;
		ticketdata = jobTicketData;
	}

	public void populateOutputArgs(Map<String, String> _params) {
	    Iterator<String> paramList = _params.keySet().iterator();
	    while (paramList.hasNext()) {
	      String nextKey = paramList.next();
	      if (nextKey.startsWith(_OUTPUT_)) {
	        String key = nextKey.substring(_OUTPUT_.length());
	        Object  obj = _params.get(nextKey);
	        String nextValue = obj.toString();
	        if (nextValue!=null) {
	          try {
	            this.setOutputArgs(key, nextValue);
	          } catch( Exception e) {
	            logFatal("updateOutputParameter severe error : "+e.getMessage());
	          }
	        }
	      }
	    }
	  
	}

}
