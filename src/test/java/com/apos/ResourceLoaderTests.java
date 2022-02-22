package com.apos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apos.resources.IResource;
import com.apos.resources.service.ResourceLoaderService;
import com.apos.utils.ResourceTools;
import com.apos.utils.WorkflowResourceTools;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
public class ResourceLoaderTests {

	private static final long SIZE =1024L;
	@Autowired
	ResourceLoaderService resourceLoad;
	@Test
	void testResourceLoader() {
		assertDoesNotThrow(()->{
			String name = "2";
			JSONArray content = WorkflowResourceTools.getStarterWorkflow();
	        IResource document = new com.apos.resources.Resource(name, SIZE, new Date());
			resourceLoad.saveResource(document, content.toString().getBytes());
			IResource resource = resourceLoad.getResource(name);
			assertEquals(content.toString(), new String(resource.getContent()));
		});
	}
	
	@Test
	void generateAndSaveWorkflow() {

		JSONObject workflowAttrs = new JSONObject();
		String  nameAttr = "name";
		String  idAttr = "resdescid";
		String  nameValue = "workflow-1";
		String  wfId="1";
		
		workflowAttrs.put(nameAttr,nameValue );
		workflowAttrs.put(idAttr,wfId );
		assertDoesNotThrow(()->{
			WorkflowResourceTools.getEmptytWorflowJson(workflowAttrs );
		});
		
		JSONArray workflow  = WorkflowResourceTools.getEmptytWorflowJson(workflowAttrs );
		
		assertEquals(nameValue,ResourceTools.getAttribute(workflow, nameAttr));
		assertEquals(wfId,ResourceTools.getAttribute(workflow, idAttr));
		assertEquals(ResourceTools.WF_REPOSITORY_WORKFLOW_TYPE, ResourceTools.getResourceType(workflow));
		
	
		JSONArray activities = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITIES_TYPE);
		
		assertEquals( ResourceTools.WF_ACTIVITIES_TYPE, ResourceTools.getResourceType(activities));
		
		JSONObject extendedAttributes = new JSONObject();
		nameAttr = "Name";
		extendedAttributes.put("pointX", "165");
		extendedAttributes.put("pointY", "60");

		JSONObject attributes = new JSONObject();
		nameValue = "Start";
		attributes.put(nameAttr, nameValue);
		attributes.put("Id", 1111);
		
		JSONArray acti = WorkflowResourceTools.getDefaultActivity(extendedAttributes, attributes );
		
		ResourceTools.addChildren(activities, acti);
		
		   JSONArray activity = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_ACTIVITY_TYPE); 
		      JSONObject impAttrs = new JSONObject();
	          impAttrs.put("Type","APPLICATION");
			  JSONObject extAttrs = new JSONObject();
			  extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.StartingNode");
			ResourceTools.addChildren(activity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
		
			
			extendedAttributes = new JSONObject();
			extendedAttributes.put("pointX", "255");
			extendedAttributes.put("pointY", "260");

			attributes = new JSONObject();
			nameValue = "End";
			attributes.put(nameAttr, nameValue);
			attributes.put("Id", 2222);
			
			JSONArray endActivity =WorkflowResourceTools.getDefaultActivity(extendedAttributes, attributes );
			    impAttrs = new JSONObject();
		        impAttrs.put("Type","APPLICATION");
				extAttrs = new JSONObject();
				extAttrs.put("pluginJava","com.sefas.workflow.plugins.java.EndingNode");
				ResourceTools.addChildren(endActivity, ResourceTools.getDefaultImplementation(extAttrs , impAttrs ));
			ResourceTools.addChildren(activities, endActivity);

			
		assertEquals(nameValue,ResourceTools.getAttribute(endActivity, nameAttr));
		
		
		
		JSONArray transitions = ResourceTools.getChildNodeOfType(workflow, ResourceTools.WF_TRANSITIONS_TYPE);
		assertEquals( ResourceTools.WF_TRANSITIONS_TYPE, ResourceTools.getResourceType(transitions));
		
		extendedAttributes = new JSONObject();
		nameAttr = "Name";
		extendedAttributes.put("pointX", "165");
		extendedAttributes.put("pointY", "150");

		attributes = new JSONObject();
		nameValue = "act-1";
		attributes.put(nameAttr, nameValue);
		attributes.put("Id", "RT-1111-2222");
		attributes.put("To", "2222");
		attributes.put("From", "1111");
		
		JSONArray transition = WorkflowResourceTools.getDefaultTransition(extendedAttributes, attributes );
		
		ResourceTools.addChildren(transitions, transition);
	
		
	}
	
}
