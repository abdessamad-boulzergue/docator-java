package com.apos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.apos.utils.ResourceTools;
import com.apos.utils.WorkflowResourceTools;

public class ResourceToolsTest {
	
	@Test
	public void testDefaultWorkflow(){
		
		
		JSONObject workflowAttrs = new JSONObject();
		String  nameAttr = "name";
		String  idAttr = "id";
		String  nameValue = "workflow-1";
		String  id="1";
		
		workflowAttrs.put(nameAttr,nameValue );
		workflowAttrs.put(idAttr,id );
		assertDoesNotThrow(()->{
			WorkflowResourceTools.getEmptytWorflowJson(workflowAttrs );
		});
		
		JSONArray workflow  = WorkflowResourceTools.getEmptytWorflowJson(workflowAttrs );
		
		assertEquals(nameValue,ResourceTools.getAttribute(workflow, nameAttr));
		assertEquals(id,ResourceTools.getAttribute(workflow, idAttr));
		assertEquals(ResourceTools.WF_REPOSITORY_WORKFLOW_TYPE, ResourceTools.getResourceType(workflow));
		
		JSONArray content = ResourceTools.getContent(workflow);
		JSONArray activities = ResourceTools.getChildNodeOfType(content, ResourceTools.WF_ACTIVITIES_TYPE);

		JSONObject extendedAttributes = new JSONObject();
		extendedAttributes.put("pointX", "135");
		extendedAttributes.put("pointY", "60");

		JSONObject attributes = new JSONObject();
		nameValue = "act-1";
		attributes.put(nameAttr, nameValue);
		
		JSONArray acti = WorkflowResourceTools.getDefaultActivity(extendedAttributes, attributes );
		
		ResourceTools.addChildren(activities, acti);
		
		   JSONArray activity = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITY_TYPE); 
		      JSONObject impAttrs = new JSONObject();
	          impAttrs.put("Type","APPLICATION");
			  JSONObject extAttrs = new JSONObject();
			  extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.StartingNode");
			ResourceTools.addChildren(activity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
		
			
			extendedAttributes = new JSONObject();
			extendedAttributes.put("pointX", "155");
			extendedAttributes.put("pointY", "160");

			attributes = new JSONObject();
			nameValue = "end-1";
			attributes.put(nameAttr, nameValue);
			
			JSONArray endActivity =WorkflowResourceTools.getDefaultActivity(extendedAttributes, attributes );
			    impAttrs = new JSONObject();
		        impAttrs.put("Type","APPLICATION");
				extAttrs = new JSONObject();
				extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.EndingNode");
				ResourceTools.addChildren(endActivity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
			ResourceTools.addChildren(activities, endActivity);

			
		assertEquals(nameValue,ResourceTools.getAttribute(endActivity, nameAttr));
		
	}
}
