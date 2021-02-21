package com.apos.utils;

import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class WorkflowResourceTools extends ResourceTools {

	

    public static final Object TYPE_APPLICATION = "APPLICATION";
	public static final String ATTR_TO = "To";
	public static final String ATTR_FROM = "From";
	public static final String ATTR_PRIORITY = "Priority";
	private static final String WF_XPDL_WORKFLOW_PLUGIN_SOURCES_TYPE = "xpdl:PluginSources";
	private static final String WF_XPDL_WORKFLOW_PLUGIN_SOURCE_TYPE = "xpdl:PluginSource";

	public static JSONArray  getDefaultTransition(JSONObject extendedAttributes, JSONObject attributes){
    	JSONArray transitionNode = createBasicElement(WF_TRANSITION_TYPE);
        setAttributes(transitionNode,attributes);

        JSONArray descriptionNode = createBasicElement(WF_Description_TYPE);

        JSONArray extendAttrsNode = createBasicElement(WF_ExtendedAttributes_TYPE);
        setAttributes(extendAttrsNode,extendedAttributes);


        addChildren(transitionNode, descriptionNode);
        addChildren(transitionNode, extendAttrsNode);
        return transitionNode;
    }
    
    public static JSONArray getDefaultActivity(JSONObject extendedAttributes, JSONObject attributes){

    	JSONArray activityNode = createBasicElement(WF_ACTIVITY_TYPE);
        setAttributes(activityNode,attributes);

        JSONArray descriptionNode = createBasicElement(WF_Description_TYPE);

        JSONArray extendAttrsNode = createBasicElement(WF_ExtendedAttributes_TYPE);
        setAttributes(extendAttrsNode,extendedAttributes);

        addChildren(activityNode, descriptionNode);
        addChildren(activityNode, extendAttrsNode);
     
        return activityNode;
    }
    
    public static JSONArray getStarterWorkflow() {
    	
    	JSONObject workflowAttrs = new JSONObject();
		String  nameValue = "new workflow";
		workflowAttrs.put(ATTR_NAME,nameValue );
		workflowAttrs.put(ATTR_RESDESC_ID,"-1" );
		return getStarterWorkflow(workflowAttrs);
    }
    	public static JSONArray getStarterWorkflow(JSONObject workflowAttrs) {
    	
    		if(!workflowAttrs.has(ATTR_RESDESC_ID)) {
    			throw new IllegalArgumentException("workflow attributes missing : resdescid");
    		}
		
		JSONArray workflow  = getEmptytWorflowJson(workflowAttrs );
	
		JSONArray activities = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITIES_TYPE);
		
		JSONObject extendedAttributes = new JSONObject();
		extendedAttributes.put(POINT_X, pointX_START);
		extendedAttributes.put(POINT_Y, pointY_START);

		JSONObject attributes = new JSONObject();
		attributes.put(ATTR_NAME, START_ACTIVITY_NAME);
		attributes.put(ATTR_ID, UUID.randomUUID().toString());
		
		JSONArray startActivity = getDefaultActivity(extendedAttributes, attributes );
		
		ResourceTools.addChildren(activities, startActivity);
		
		   JSONArray activity = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITY_TYPE); 
		      JSONObject impAttrs = new JSONObject();
	          impAttrs.put("Type","APPLICATION");
			  JSONObject extAttrs = new JSONObject();
			  extAttrs.put("pluginJava","com.apos.workflow.plugins.java.StartingNode");
			ResourceTools.addChildren(activity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
		
			
			extendedAttributes = new JSONObject();
			extendedAttributes.put("pointX", pointX_END);
			extendedAttributes.put("pointY", pointY_END);

			attributes = new JSONObject();
			attributes.put(ATTR_NAME, "End");
			attributes.put("Id", UUID.randomUUID().toString());
			
			JSONArray endActivity = getDefaultActivity(extendedAttributes, attributes );
			    impAttrs = new JSONObject();
		        impAttrs.put("Type","APPLICATION");
				extAttrs = new JSONObject();
				extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.EndingNode");
				ResourceTools.addChildren(endActivity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
			ResourceTools.addChildren(activities, endActivity);

			
		JSONArray transitions = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_TRANSITIONS_TYPE);
		
		extendedAttributes = new JSONObject();
		extendedAttributes.put("pointX", pointX_TRANSITION);
		extendedAttributes.put("pointY", pointY_TRANSITION);

		attributes = new JSONObject();
		attributes.put(ATTR_NAME, "activity-1");
		attributes.put("Id", UUID.randomUUID().toString());
		attributes.put("To", getAttribute(endActivity, ATTR_ID));
		attributes.put("From", getAttribute(startActivity, ATTR_ID));
		
		JSONArray transition = getDefaultTransition(extendedAttributes, attributes );
		
		ResourceTools.addChildren(transitions, transition);
		return workflow;
    }
    
   public static JSONArray setJobConfiguration(JSONArray workflow,String id ) {
	   
	   JSONArray content = getContent(workflow);
	   JSONArray jobConfig = getChildNodeOfType(content, WF_REPOSITORY_WORKFLOW_JOB_CONFIG);
	   JSONObject attrs = null;
	   if(jobConfig != null) {
		   attrs = getAttributes(jobConfig);
	   }
	   else {
		   jobConfig = createBasicElement(WF_REPOSITORY_WORKFLOW_JOB_CONFIG,false);
		   attrs = new JSONObject();
	   }

		attrs.put(ATTR_RESDESC_ID, id);
		setAttributes(jobConfig,attrs);
		
		addChildren(content, jobConfig);
		
	   return workflow;
   }
    
	public static JSONArray getEmptytWorflowJson(JSONObject workflowAttrs ) {

		JSONArray workflowNode = createBasicElement(WF_REPOSITORY_WORKFLOW_TYPE,true);
		setAttributes(workflowNode,workflowAttrs);
		JSONArray content = getContent(workflowNode);
		
		JSONArray xpdlNode = createBasicElement(WF_XPDL_TYPE);
		JSONArray xpdlPackageNode = createBasicElement(WF_XPDL_PACKAGE_TYPE);
		JSONArray xpdlProcessesNode = createBasicElement(WF_XPDL_WORKFLOW_PROCESSES_TYPE);
		JSONArray xpdlProcessNode = createBasicElement(WF_XPDL_WORKFLOW_PROCESS_TYPE);
		JSONArray activitiesNode = createBasicElement(WF_ACTIVITIES_TYPE);
		JSONArray transitionsNode = createBasicElement(WF_TRANSITIONS_TYPE);

		addChildren(xpdlProcessNode,activitiesNode);
        addChildren(xpdlProcessNode, transitionsNode);
        addChildren(xpdlProcessesNode, xpdlProcessNode);
        addChildren(xpdlPackageNode, xpdlProcessesNode);
        addChildren(xpdlNode, xpdlPackageNode);

        addChildren(content, xpdlNode);

		return workflowNode;
	}
	
	public static JSONArray getPackageTag(JSONArray jsArray) {
		if(ResourceTools.WF_XPDL_PACKAGE_TYPE.equals(ResourceTools.getResourceType(jsArray))){
			return jsArray;
		}
		JSONArray child = ResourceTools.getChildren(jsArray);
		Iterator<Object> iter = child.iterator();
		if(iter.hasNext())
		    return getPackageTag((JSONArray) iter.next());
		else 
			return null;
	}

	public static JSONArray getWorkflowProcesses(JSONArray workflowJson) {
		if(isBasicElement(workflowJson)) {
			JSONArray packg = getPackageTag(workflowJson);
			if(packg!=null) {
				return getChildNodeOfType(packg, WF_XPDL_WORKFLOW_PROCESSES_TYPE);
			}
			return null;
		}else {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
		}
		
	}

	public static JSONArray getWorkflowPluginSources(JSONArray workflowJson) {
		if(isBasicElement(workflowJson)) {
			JSONArray packg = getPackageTag(workflowJson);
			if(packg!=null) {
				return getChildNodeOfType(packg, WF_XPDL_WORKFLOW_PLUGIN_SOURCES_TYPE);
			}
			return null;
		}else {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
		}
		
	}


}
