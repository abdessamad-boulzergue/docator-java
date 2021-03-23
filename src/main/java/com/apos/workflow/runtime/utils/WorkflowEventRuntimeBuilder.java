package com.apos.workflow.runtime.utils;

import org.json.JSONObject;

import com.apos.workflow.model.Activity;

public class WorkflowEventRuntimeBuilder {

	JSONObject object;
	private static final  String CONTEXT="context";
	public WorkflowEventRuntimeBuilder() {
		object = new JSONObject();
	}
	public WorkflowEventRuntimeBuilder(JSONObject obj) {
		object = obj;
	}
	public String getContext(){
		if(object!=null && object.has(CONTEXT)) {
			return object.getString(CONTEXT);
		}else {
			return null;
		}
	}
	public WorkflowEventRuntimeBuilder setActivity(Activity activity) {
		object.put("activityId", activity.getId());
		object.put("activityName", activity.getName());
		return this;
	}
	public WorkflowEventRuntimeBuilder setState(int state) {
		object.put("state", state);
		return this;
	}
	
	public WorkflowEventRuntimeBuilder append(String key, String value) {
		object.put(key, value);
		return this;
	}
	
	public JSONObject build() {
		return object;
	}
	
	@Override
	public String toString() {
		return object.toString();
	}
	public WorkflowEventRuntimeBuilder setContext(String context) {
		object.put(CONTEXT, context);
		return this;
	}
}
