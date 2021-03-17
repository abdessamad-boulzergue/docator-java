package com.apos.workflow.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.apos.plugins.IPluginLoad;
import com.apos.utils.ResourceTools;
import com.apos.utils.WorkflowResourceTools;
import com.apos.workflow.model.Activity;
import com.apos.workflow.model.Transition;
import com.apos.workflow.plugin.WorkflowScriptlet;

public class WorkflowProcess {
	  static Logger  logger        = Logger.getLogger(WorkflowScriptlet.class);

	private IPluginLoad loader;
	private Map _runningEnvironment;
	private boolean isLoadFinish;
	private boolean isChanged = true;
	private String id;
	private String name;
	private LinkedHashMap<String, Activity>          activities                     = new LinkedHashMap<String, Activity>();
	private LinkedHashMap<String, Transition>          transitions                     = new LinkedHashMap<String, Transition>();
	private Activity starter;

	public WorkflowProcess(IPluginLoad pluginLoader) {
		this.loader = pluginLoader;
	}

	public void setRunningEnvironment(Map runningEnvironment) {
	    _runningEnvironment = runningEnvironment;
		
	}

	public void load(JSONArray workflowProcess) {
		isLoadFinish = false;
		loadHeader(workflowProcess);
	    loadActivities(workflowProcess);
	    loadTransition(workflowProcess);
	    isLoadFinish = true;
	    isChanged  = false;
	}

	private void loadTransition(JSONArray workflowProcess) {

		JSONArray transitionsTag = WorkflowResourceTools.getChildNodeOfType(workflowProcess, ResourceTools.WF_TRANSITIONS_TYPE);
		JSONArray jsTransitions = WorkflowResourceTools.getChildren(transitionsTag);
		Iterator<Object> transitionsIter = jsTransitions.iterator();
		while(transitionsIter.hasNext()) {
			Transition tr = new Transition();
			tr.setProcess(this);
			tr.load(loader, (JSONArray) transitionsIter.next());
	        transitions.put(tr.getId(), tr);
		}
		
	}

	private void loadActivities(JSONArray workflowProcess) {
		JSONArray activitiesTag = WorkflowResourceTools.getChildNodeOfType(workflowProcess, ResourceTools.WF_ACTIVITIES_TYPE);
		JSONArray jsActivities = WorkflowResourceTools.getChildren(activitiesTag);
		Iterator<Object> activitiesIter = jsActivities.iterator();
		while(activitiesIter.hasNext()) {
			Activity activity = new Activity();
	        activity.setProcess(this);
	        activity.load(loader, (JSONArray) activitiesIter.next());
	        activities.put(activity.getId(), activity);
		}
	}

	private void loadHeader(JSONArray workflowProcess) {
		this.id = ResourceTools.getAttribute(workflowProcess, ResourceTools.ATTR_ID);
		this.name = ResourceTools.getAttribute(workflowProcess, ResourceTools.ATTR_NAME);
		loadDescription(workflowProcess);
	}
	
	private void loadDescription(JSONArray workflowProcess) {
		logger.warn("loadDescription not implemented");
	}
	public String getName() {
		return this.name;
	}
	public Activity getActivity(String id) {
		return activities.get(id);
	}

	public Activity getStarter() {
		if(starter ==null) {
			Iterator<Activity> iter = activities.values().iterator();
			while(iter.hasNext()) {
				Activity act = iter.next();
				if(act.isStarter()) {
					starter =act;
					break;
				}
			}
		}
		return starter;
	}

}
