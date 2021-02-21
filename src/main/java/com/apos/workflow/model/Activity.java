package com.apos.workflow.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apos.plugins.IPluginLoad;
import com.apos.utils.ResourceTools;
import com.apos.workflow.runtime.JobTicketData;
import com.apos.workflow.runtime.JobTicketRunner;
import com.apos.workflow.runtime.WorkflowProcess;

public class Activity {

	private Implementation  implementation   = null;
	private WorkflowProcess _workflowProcess;
	private JSONObject extendedAttributes;
	private JSONObject attributes;
	private JobTicketRunner runner;
	private ActivityGraph graph= new ActivityGraph();
	
	public void execute() {
		implementation.execute();
	}

	public void setProcess(WorkflowProcess workflowProcess) {
		_workflowProcess = workflowProcess;
	}

	public void load(IPluginLoad loader, JSONArray jsActivity) {
		this.loadExtendedAttributes(jsActivity);
        this.loadCommonAttributes(jsActivity);
        this.loadImplementation(loader,jsActivity);
	}
	

	private void loadImplementation(IPluginLoad loader, JSONArray jsActivity) {
		JSONArray jsonImplement = ResourceTools.getChildNodeOfType(jsActivity,ResourceTools.WF_Implementation_TYPE);
	   if(ResourceTools.isBasicElement(jsonImplement)) {
		   implementation = new Implementation(this);
		   implementation.load(loader, jsonImplement);
	   }
	}

	private void loadCommonAttributes(JSONArray jsActivity) {
		this.attributes =  ResourceTools.getAttributes(jsActivity);
	}
	public String getId() {
		return String.valueOf(this.attributes.get(ResourceTools.ATTR_ID));
	}
	private void loadExtendedAttributes(JSONArray jsActivity) {
		JSONArray extendedAttrNode = ResourceTools.getChildNodeOfType(jsActivity,ResourceTools.WF_ExtendedAttributes_TYPE);
	    this.extendedAttributes=ResourceTools.getAttributes(jsActivity);
	    
	}

	public boolean isStarter() {
		return implementation.isStarter();
	}

	public void setRunner(JobTicketRunner runner) {
		this.runner = runner;
	}

	public void execute(JobTicketRunner jobTicketRunner, JobTicketData jobTicketData) {
		implementation.execute(jobTicketRunner, jobTicketData);
	}

	public ActivityGraph getGraph() {
		return graph;
	}

	
}
