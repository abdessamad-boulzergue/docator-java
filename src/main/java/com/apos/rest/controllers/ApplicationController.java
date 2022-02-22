package com.apos.rest.controllers;

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
import com.apos.utils.ResourceTools;

@RestController
@RequestMapping(path = "application")
@CrossOrigin
public class ApplicationController {

	@Autowired
	   ResourcesService resourceService;
	   Logger logger = LoggerFactory.getLogger(ApplicationController.class);

	   @PostMapping("/new")
		public ResponseEntity<Resource> createApplication() {
			
			Resource resource =  Resource.getApplicationResource();
			Resource savedResource = resourceService.saveResource(resource );
			JSONArray content = ResourceTools.getResource(ResourceType.TYPE_APPLICATION,savedResource.toJson());
			resourceService.writeResourceTofile(String.valueOf(savedResource.getId()), content.toString());
			
			return ResponseEntity.ok(savedResource);
			
		}
	   
	   @GetMapping
	   public ResponseEntity<String> load(@RequestParam(name="id") String id){
		   String resource = resourceService.readResourceFromFile(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(resource );
	   }
	@PostMapping
	public ResponseEntity<String> save(@RequestBody String jsonString){

		try {
			
			String content = URLDecoder.decode(jsonString,"utf-8");
			JSONArray json = new JSONArray(content);
			JSONObject attributes = json.getJSONObject(1);
			ResourceType type = resourceService.getType(ResourceType.TYPE_APPLICATION);
			Resource resource = Resource.from(attributes);
			resource.setType(type);
			Resource savedResource = resourceService.saveResource(resource);
			if(savedResource!=null && savedResource.getId()>0) {
				String versionName = savedResource.getMaxVersion().getName();
				attributes.put("version", versionName);				
				resourceService.writeResourceTofile(String.valueOf(savedResource.getId()), json.toString());
				
			}else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("save failed") ; 
			}
			
		} catch (Exception e) {
			  String msg = (e!=null)? e.getMessage() : "";
			 logger.error("save document : exception ".concat(msg));
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg) ; 
		}
	     return ResponseEntity.status(HttpStatus.OK).body("") ;  
	}
}
