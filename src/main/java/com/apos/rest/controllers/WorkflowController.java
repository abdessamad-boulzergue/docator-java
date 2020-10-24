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

import com.apos.resources.ResourceLoaderService;
import com.apos.rest.dto.WorkflowData;

@RestController
@CrossOrigin
@RequestMapping("/workflow")
public class WorkflowController {
   Logger logger = LoggerFactory.getLogger(WorkflowController.class);
   
   @Autowired
   ResourceLoaderService resourceLoader;
   
   
   @GetMapping("/load")
   public ResponseEntity<String> load(@RequestParam(name="id") String id){
	   String workflow=null;
	try {
		workflow = resourceLoader.readResource(id);
	} catch (IOException e) {
		e.printStackTrace();
	}
	return ResponseEntity.status(HttpStatus.OK).body(workflow );
   }
   
	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody WorkflowData workflowJson) {
		String result=null;
		try {
			
			String content = URLDecoder.decode(workflowJson.getWorkflow(),"utf-8");
			JSONArray json = new JSONArray(content);
			JSONObject name = json.getJSONObject(1);
			result =  resourceLoader.writeResource(name.getString("resdescid"), content);
			
			if(result == null || result.isEmpty()) {
				 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("save failed") ; 
			}
			
		} catch (IOException e) {
			 logger.error("save workflow : exception ".concat(e.getMessage()));
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()) ; 
		}
	     return ResponseEntity.status(HttpStatus.OK).body(result) ; 

	}
}
