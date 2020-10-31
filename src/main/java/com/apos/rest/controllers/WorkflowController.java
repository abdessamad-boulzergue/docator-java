package com.apos.rest.controllers;

import java.io.IOException;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apos.models.Resource;
import com.apos.models.ResourceType;
import com.apos.rest.controllers.service.ResourcesService;
import com.apos.rest.dto.WorkflowData;
import com.apos.utils.ResourceTools;

@RestController
@CrossOrigin
@RequestMapping("/workflow")
public class WorkflowController {
   Logger logger = LoggerFactory.getLogger(WorkflowController.class);
   
   @Autowired
   ResourcesService resourceService; 
   
   
   @GetMapping("/load")
   public ResponseEntity<String> load(@RequestParam(name="id") String id){
	   String workflow=null;
	
		workflow = resourceService.readResourceFromFile(id);
	
	return ResponseEntity.status(HttpStatus.OK).body(workflow );
   }
   
	@PostMapping("/new")
	public ResponseEntity<Resource> createWorkflow() {
		
		Resource resource =  Resource.getWorkflowResource();
		Resource savedResource = resourceService.saveResource(resource );
		
		JSONArray content = ResourceTools.getStarterWorkflow(savedResource.toJson());
		resourceService.writeResourceTofile(String.valueOf(savedResource.getId()), content.toString());
		
		resourceService.readResourceFromFile(String.valueOf(savedResource.getId()));
		
		return ResponseEntity.ok(savedResource);
		
	}
	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody WorkflowData workflowJson) {
		String result=null;
		try {
			
			String content = URLDecoder.decode(workflowJson.getWorkflow(),"utf-8");
			
			JSONArray json = new JSONArray(content);
			JSONObject attributes = json.getJSONObject(1);
			ResourceType type = resourceService.getType(ResourceType.TYPE_WORKFLOW);
			Resource resource = Resource.from(attributes);
			resource.setType(type);
			Resource savedResource = resourceService.saveResource(resource);
			if(savedResource!=null && savedResource.getId()>0) {
			
				String versionName = savedResource.getMaxVersion().getName();
				attributes.put("version", versionName);
				attributes.put("resdescid", String.valueOf(savedResource.getId()));
				
				result =  resourceService.writeResourceTofile(attributes.getString("resdescid"), json.toString());

				if(result == null || result.isEmpty()) {
					 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("save failed") ; 
				}
				
			}else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("save failed") ; 
			}
			
		} catch (IOException e) {
			 logger.error("save workflow : exception ".concat(e.getMessage()));
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()) ; 
		}
	     return ResponseEntity.status(HttpStatus.OK).body(result) ; 

	}
}
