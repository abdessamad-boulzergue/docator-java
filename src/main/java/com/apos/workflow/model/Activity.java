package com.apos.workflow.model;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apos.plugins.EnginesScriptlet;
import com.apos.plugins.IPluginLoad;
import com.apos.utils.ResourceTools;
import com.apos.workflow.runtime.JobTicketData;
import com.apos.workflow.runtime.JobTicketRunner;
import com.apos.workflow.runtime.WorkflowProcess;

public class Activity {

	private static final String SCRIPTPARAMETER_PREFIX = "SCRIPTPARAMETER.";
	private Implementation implementation = null;
	private WorkflowProcess _workflowProcess;
	private JSONObject extendedAttributes;
	private JSONObject attributes;
	private JobTicketRunner runner;
	private ActivityGraph graph = new ActivityGraph();

	public void execute() {
		implementation.execute();
	}

	public void setProcess(WorkflowProcess workflowProcess) {
		_workflowProcess = workflowProcess;
	}

	public void load(IPluginLoad loader, JSONArray jsActivity) {
		this.loadExtendedAttributes(jsActivity);
		this.loadCommonAttributes(jsActivity);
		this.loadImplementation(loader, jsActivity);
		this.setScripletAttribute();

	}

	 public EnginesScriptlet getScriptlet() {
		    EnginesScriptlet scriptlet = implementation.getApplication().getInstance().getImplementation();
		    return scriptlet;
		  }
	private void setScripletAttribute() {
		//EnginesScriptlet scriplet = this.getScriptlet();
		//scriplet.getScripletArgs();
	}

	private void loadImplementation(IPluginLoad loader, JSONArray jsActivity) {
		JSONArray jsonImplement = ResourceTools.getChildNodeOfType(jsActivity, ResourceTools.WF_Implementation_TYPE);
		if (ResourceTools.isBasicElement(jsonImplement)) {
			implementation = new Implementation(this);
			implementation.load(loader, jsonImplement);
		}
	}

	private void loadCommonAttributes(JSONArray jsActivity) {
		this.attributes = ResourceTools.getAttributes(jsActivity);
	}

	public String getId() {
		return String.valueOf(this.attributes.get(ResourceTools.ATTR_ID));
	}
	public String getName() {
		return String.valueOf(this.attributes.get(ResourceTools.ATTR_NAME));
	}

	private void loadExtendedAttributes(JSONArray jsActivity) {
		JSONArray extendedAttrNode = ResourceTools.getChildNodeOfType(jsActivity,
				ResourceTools.WF_ExtendedAttributes_TYPE);
		this.extendedAttributes = ResourceTools.getAttributes(extendedAttrNode);

	}

	public boolean isStarter() {
		return implementation.isStarter();
	}

	public void setRunner(JobTicketRunner runner) {
		this.runner = runner;
	}

	public void execute(JobTicketRunner jobTicketRunner, JobTicketData jobTicketData) {
		this.populateJobTicketData(jobTicketData);
		implementation.execute(jobTicketRunner, jobTicketData);
	}

	private void populateJobTicketData(JobTicketData jobTicketData) {

	    Set<String> dataKeys = extendedAttributes.keySet();
	    for (String key : dataKeys) {
	      if (key.startsWith(SCRIPTPARAMETER_PREFIX)) {
	        String value = extendedAttributes.getString(key);
	        key = key.substring(SCRIPTPARAMETER_PREFIX.length());
	        if (jobTicketData != null) {
	          String nameId = getName() + "_" + getId();
	          String paramKey = buildStepKey(nameId, key);
	          jobTicketData.setData(paramKey, value);
	        }
	      }
	    }
	  
	}

	private String buildStepKey(String nameId, String key) {
	    StringBuilder buf = new StringBuilder();
	    if (nameId != null) {
	      buf.append(nameId.replace(' ', '_')).append('.');
	    }
	    buf.append(key);
	    return buf.toString();
	  }

	public ActivityGraph getGraph() {
		return graph;
	}

}
