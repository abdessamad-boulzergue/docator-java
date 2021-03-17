package com.apos.workflow.model;

import java.util.Iterator;
import java.util.Map;

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
		    		 try {
						
					
		    		  if(scriptlet instanceof WorkflowScriptlet) {
		    			  WorkflowScriptlet wfScriptlet = (WorkflowScriptlet) scriptlet;
		    			  populate(wfScriptlet,jobTicketRunner,jobTicketData,parentImplementation.getActivity());
		    			  jobTicketData.updateScriplet(wfScriptlet);
		    			  if(wfScriptlet instanceof RemoteScriptlet) {
		    				  ((RemoteScriptlet)wfScriptlet).setPluginSource(this.extendedAttributes.getString(IPlugin.PLUGIN_SRC_ID));
		    			  }
		    			  wfScriptlet.run();
		    			  
		    			  jobTicketData.populateWithScripletOuput(wfScriptlet.getOutputArgs());
		    			  
					      instance.clear(); 
					      
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		    	 }
		    	  
		      }
		     
		}
	}

	private void populate(WorkflowScriptlet wfScriptlet, JobTicketRunner jobTicketRunner, JobTicketData jobTicketData,
			Activity activity) throws Exception {
		wfScriptlet.populateRuntimeArgs(jobTicketRunner,jobTicketData);
		wfScriptlet.populateOutputArgs(jobTicketData.getRunningEnvironment());
		String nameId = activity.getName() + "_" + activity.getId();
		nameId = nameId.replace(' ', '_');
		updateScripletArgs(wfScriptlet,nameId,jobTicketData.getRunningEnvironment());
	}
	private void updateScripletArgs(WorkflowScriptlet wfScriptlet, String nameId, Map<String, String> params) throws Exception {
		Iterator<String> paramList = params.keySet().iterator();
	    while (paramList.hasNext()) {
	      String nextKey = paramList.next();
	      String scriptletName = nextKey;
	      String separator = nameId.concat(".");
	      if (nextKey.startsWith(separator)) {
	        scriptletName = nextKey.substring(separator.length());
	      }
	      if (scriptletName != null && !scriptletName.isEmpty()) {
	    	 Object obj =  params.get(nextKey);
	        String nextValue = obj.toString();
	        wfScriptlet.setScriptletArgs(scriptletName, nextValue,false);
	        
	      }
	    }
	}
	public IPlugin getInstance() {
		return instance;
	}
}

