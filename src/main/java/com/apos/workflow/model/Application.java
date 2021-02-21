package com.apos.workflow.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apos.plugins.EnginesScriptlet;
import com.apos.plugins.IPlugin;
import com.apos.plugins.IPluginLoad;
import com.apos.plugins.RemoteScriptlet;
import com.apos.utils.ResourceTools;
import com.apos.utils.WorkflowResourceTools;
import com.apos.workflow.plugin.IApplication;
import com.apos.workflow.plugin.WorkflowScriptlet;
import com.apos.workflow.runtime.JobTicketData;
import com.apos.workflow.runtime.JobTicketRunner;

public class Application implements IApplication {

	private IPlugin instance = null;
	private Implementation parentImplementation;
	private JSONObject attributes;
	private JSONObject extendedAttributes;

	public Application(Implementation implementation) {
		parentImplementation = implementation;
	}

	public void execute() {
		//Script instance.getScript();
	}

	public void load(IPluginLoad loader, JSONArray jsTool) {
		this.loadCommonAttributes(jsTool);
        this.loadExtendedAttributes(jsTool);
        String type = attributes.getString(ResourceTools.ATTR_TYPE);
        if(type.equals(WorkflowResourceTools.TYPE_APPLICATION)) {
        	IPlugin newInstance =  WorkflowModelFactory.buildPluginComponent(loader,this.extendedAttributes);
        	 setInstance(newInstance);
        }
        
	}
	private void setInstance(IPlugin newInstance) {
	    instance = newInstance;
	    if (instance != null) {
	      instance.addFactoryInformation(extendedAttributes);
	      instance.setApplicationParent(this);
	    }
	  }

	private void loadCommonAttributes(JSONArray jsActivity) {
		this.attributes =  ResourceTools.getAttributes(jsActivity);
	}

	private void loadExtendedAttributes(JSONArray jsActivity) {
		JSONArray extendedAttrNode = ResourceTools.getChildNodeOfType(jsActivity,ResourceTools.WF_ExtendedAttributes_TYPE);
	    this.extendedAttributes=ResourceTools.getAttributes(extendedAttrNode);
	    
	}

	public boolean isStarter() {
		return instance.isStarting();
	}

	public void execute(JobTicketRunner jobTicketRunner, JobTicketData jobTicketData, Activity act) {		

		if(instance != null) {
		      EnginesScriptlet scriptlet = instance.getImplementation();
		      if(scriptlet!=null) {
		    	  synchronized(scriptlet) {
		    		  if(scriptlet instanceof WorkflowScriptlet) {
		    			  WorkflowScriptlet wfScriptlet = (WorkflowScriptlet) scriptlet;
		    			  wfScriptlet.populate(jobTicketRunner,jobTicketData);
		    			  if(wfScriptlet instanceof RemoteScriptlet) {
		    				  ((RemoteScriptlet)wfScriptlet).setPluginSource(this.extendedAttributes.getString(IPlugin.PLUGIN_SRC_ID));
		    			  }
		    			  wfScriptlet.run();
					      instance.clear(); 
		    		  }
		    	 }
		    	  
		      }
		     
		}
	}
}

