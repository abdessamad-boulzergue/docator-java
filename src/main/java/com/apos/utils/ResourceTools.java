package com.apos.utils;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResourceTools {

	
	public static final int INDEX_TYPE = 0;
	public static final int INDEX_ATTRIBUTES = 1;
	public static final int INDEX_CHILDREN = 2;

	public static final String CONTENT = "CONTENT";

	 public final static String WF_REPOSITORY_WORKFLOW_TYPE  = "repository:Workflow";
	 public static final String WF_Description_TYPE = "xpdl:Description";
	 public static final String  WF_ExtendedAttributes_TYPE = "xpdl:ExtendedAttributes";
	public static final String WF_Implementation_TYPE = "xpdl:Implementation";
	public static final String WF_Tool_TYPE = "xpdl:Tool";
	public static final String WF_ACTIVITIES_TYPE = "xpdl:Activities";
	public static final  String WF_ACTIVITY_TYPE = "xpdl:Activity";
	public static final String WF_TRANSITIONS_TYPE = "xpdl:Transitions";
	public static final String WF_TRANSITION_TYPE = "xpdl:Transition";
	public static final String WF_PLUGIN_PYTHON_TYPE = "pluginPython";
    public static final String WF_XPDL_TYPE = "XPDL";
    public static final String WF_XPDL_PACKAGE_TYPE = "xpdl:Package";
    public static final String WF_XPDL_WORKFLOW_PROCESSES_TYPE = "xpdl:WorkflowProcesses";
    public static final String WF_XPDL_WORKFLOW_PROCESS_TYPE = "xpdl:WorkflowProcess";
	private static final String pointX_START = "250";
	private static final String pointY_START = "50";
	
	private static final String pointX_TRANSITION = "250";
	private static final String pointY_TRANSITION = "150";
	
	private static final String pointX_END = "300";
	private static final String pointY_END = "250";
	private static final String START_ACTIVITY_NAME = "Start";
    
    
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
    	String  nameAttr = "name";
		String  idAttr = "resdescid";
		String  nameValue = "new workflow";
		String  wfId="-1";
		workflowAttrs.put(nameAttr,nameValue );
		workflowAttrs.put(idAttr,wfId );
		return getStarterWorkflow(workflowAttrs);
    }
    	public static JSONArray getStarterWorkflow(JSONObject workflowAttrs) {
    	
    		if(!workflowAttrs.has("resdescid")) {
    			throw new IllegalArgumentException("workflow attributes missing : resdescid");
    		}
		
		JSONArray workflow  = ResourceTools.getEmptytWorflowJson(workflowAttrs );
	
		JSONArray activities = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITIES_TYPE);
		
		JSONObject extendedAttributes = new JSONObject();
		 String nameAttr = "Name";
		extendedAttributes.put("pointX", pointX_START);
		extendedAttributes.put("pointY", pointY_START);

		JSONObject attributes = new JSONObject();
		attributes.put(nameAttr, START_ACTIVITY_NAME);
		attributes.put("Id", UUID.randomUUID().toString());
		
		JSONArray startActivity = ResourceTools.getDefaultActivity(extendedAttributes, attributes );
		
		ResourceTools.addChildren(activities, startActivity);
		
		   JSONArray activity = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITY_TYPE); 
		      JSONObject impAttrs = new JSONObject();
	          impAttrs.put("Type","APPLICATION");
			  JSONObject extAttrs = new JSONObject();
			  extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.StartingNode");
			ResourceTools.addChildren(activity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
		
			
			extendedAttributes = new JSONObject();
			extendedAttributes.put("pointX", pointX_END);
			extendedAttributes.put("pointY", pointY_END);

			attributes = new JSONObject();
			attributes.put(nameAttr, "End");
			attributes.put("Id", UUID.randomUUID().toString());
			
			JSONArray endActivity =ResourceTools.getDefaultActivity(extendedAttributes, attributes );
			    impAttrs = new JSONObject();
		        impAttrs.put("Type","APPLICATION");
				extAttrs = new JSONObject();
				extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.EndingNode");
				ResourceTools.addChildren(endActivity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
			ResourceTools.addChildren(activities, endActivity);

			
		
		
		
		JSONArray transitions = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_TRANSITIONS_TYPE);
		
		extendedAttributes = new JSONObject();
		nameAttr = "Name";
		extendedAttributes.put("pointX", pointX_TRANSITION);
		extendedAttributes.put("pointY", pointY_TRANSITION);

		attributes = new JSONObject();
		String nameValue = "act-1";
		attributes.put(nameAttr, nameValue);
		attributes.put("Id", UUID.randomUUID().toString());
		attributes.put("To", getAttribute(endActivity, "Id"));
		attributes.put("From", getAttribute(startActivity, "Id"));
		
		JSONArray transition = ResourceTools.getDefaultTransition(extendedAttributes, attributes );
		
		ResourceTools.addChildren(transitions, transition);
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

	
	public static JSONArray getContent(JSONArray basicElement) {
		return getChildNodeOfType(basicElement, CONTENT);
	}

	public static JSONArray getDefaultImplementation(JSONObject extendedAttributes, JSONObject attributes){
		JSONArray implementationNode = createBasicElement(WF_Implementation_TYPE);
		JSONArray toolNode = getDefaultXpdlTool(extendedAttributes,attributes);
        addChildren(implementationNode,toolNode);

        return implementationNode;
      }
	
	public static JSONArray getDefaultXpdlTool(JSONObject extendedAttributes,JSONObject attributes){
		  JSONArray toolNode = createBasicElement(WF_Tool_TYPE);
          setAttributes(toolNode,attributes);
          JSONArray extendAttrsToolNode = createBasicElement(WF_ExtendedAttributes_TYPE);
          setAttributes(extendAttrsToolNode,extendedAttributes);
          addChildren(toolNode, extendAttrsToolNode);
          return toolNode;
        }
	
	public static JSONArray getChildNodeOfType(JSONArray basicElement, String typeString) {
		
		if(typeString.equals(getResourceType(basicElement))) {
			return basicElement;
		}
        JSONArray contentArray = getChildren(basicElement);
        JSONArray nodeOfType = null;
        for(Object child : contentArray){
        	if(child instanceof JSONArray && typeString.equals(getResourceType((JSONArray)child)) ) {
        		nodeOfType = (JSONArray) child;
        	}else if(child instanceof JSONArray) {
        		
				for(Object litleChilde : getChildren((JSONArray)child)) {
					JSONArray found = getChildNodeOfType((JSONArray)litleChilde ,typeString);
				 if(found!=null)
					return found;
        	}
        	}
        }
 
        return nodeOfType;
	}

	public static String getAttribute (JSONArray resource, String attributeName) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		
        if( resource.getJSONObject(INDEX_ATTRIBUTES).has(attributeName)) {
        	return resource.getJSONObject(INDEX_ATTRIBUTES).getString(attributeName);
        }
        else {
        	return "";
        }
    }

	public static String getResourceType(JSONArray resource) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ] : "+resource.toString()) ;
        }
		return resource.getString(INDEX_TYPE);
	}


	public static JSONArray  getChildren(JSONArray resource) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		return (JSONArray) resource.get(INDEX_CHILDREN);
	}


	public static void addChildren(JSONArray resource, JSONArray children) {
		
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		
		((JSONArray)resource.get(INDEX_CHILDREN)).put(children);
		
	}

	public static void setAttributes(JSONArray resource, JSONObject attrs) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		resource.put(INDEX_ATTRIBUTES,attrs);
	}

	public static boolean isBasicElement(JSONArray resource) {
		
		return 
                resource.length() == 3
                && resource.get(INDEX_TYPE) instanceof String
                && resource.get(INDEX_ATTRIBUTES) instanceof JSONObject
                && resource.get(INDEX_CHILDREN) instanceof JSONArray;
		
	}

	public static JSONArray createBasicElement(String beType) {
		return createBasicElement(beType,false);
	}
		public static JSONArray createBasicElement(String beType, boolean withContent) {
		JSONArray content = new JSONArray();
		JSONArray basicElement = new JSONArray();
		basicElement.put(beType);
		
		if(withContent == true) {
			content.put( createBasicElement(CONTENT , false));
		}
		basicElement.put(new JSONObject());
		basicElement.put(content);
		
        return basicElement;
	}
	
}
