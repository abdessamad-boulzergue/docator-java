package com.apos.workflow.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apos.plugins.IPlugin;
import com.apos.plugins.IPluginLoad;
import com.apos.utils.ResourceTools;
import com.apos.utils.WorkflowResourceTools;
import com.apos.workflow.runtime.WorkflowProcess;

public class Transition {

	private Implementation  implementation   = null;
	private WorkflowProcess workflowProcess;
	private JSONObject extendedAttributes;
	private JSONObject attributes;
	private Activity targetAcivity;
	private Activity sourceAcivity;
	
	public void execute() {
		implementation.execute();
	}

	public void setProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	public void load(IPluginLoad loader, JSONArray jsTransition) {
		this.loadExtendedAttributes(jsTransition);
        this.loadCommonAttributes(jsTransition);
        targetAcivity = this.workflowProcess.getActivity(getTargetActivityId());
        sourceAcivity = this.workflowProcess.getActivity(getSourceActivityId());
        targetAcivity.getGraph().addTransitionToMe(this);
        sourceAcivity.getGraph().addTransitionFromMe(this);
        //IPlugin newInstance = WorkflowModelFactory.buildPluginComponent(loader, extendedAttributes);

	}
	
	public String getTargetActivityId(){
		return String.valueOf(this.attributes.get(WorkflowResourceTools.ATTR_TO));
	}
	public String getSourceActivityId(){
		return String.valueOf(this.attributes.get(WorkflowResourceTools.ATTR_FROM));
	}
	public String getPriority(){
		return this.extendedAttributes.getString(WorkflowResourceTools.ATTR_PRIORITY);
	}


	private void loadCommonAttributes(JSONArray jsActivity) {
		this.attributes =  ResourceTools.getAttributes(jsActivity);
	}
	public String getId() {
		return this.attributes.getString(ResourceTools.ATTR_ID);
	}
	private void loadExtendedAttributes(JSONArray jsActivity) {
		JSONArray extendedAttrNode = ResourceTools.getChildNodeOfType(jsActivity,ResourceTools.WF_ExtendedAttributes_TYPE);
	    this.extendedAttributes=ResourceTools.getAttributes(jsActivity);
	    
	}

	public Activity getTargetActivity() {
		return targetAcivity;
	}

	
}
