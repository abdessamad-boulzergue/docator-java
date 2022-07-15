package com.apos.rest.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apos.rest.controllers.service.ResourcesService;

@RestController()
@CrossOrigin
@RequestMapping("/jobTicket")
public class JobTicketController {

	@Autowired
	   ResourcesService resourceService;
	
	@GetMapping("config/{configId}")
	public ResponseEntity<String> getJobTiecktConfig(@PathVariable(value = "configId") String configId){
		
		try {
			if(configId.equals("-1")) {
				JSONObject result= new JSONObject();
				result.put("PYTHONHOME", "C:/Python/Python37");
				result.put("environnementPath", "C:/abdos");
				return ResponseEntity.status(HttpStatus.OK).body(result.toString());
			}else {
				String content = resourceService.readResourceFromJsonFile(configId);
				JSONObject result= new JSONObject(content);
				return ResponseEntity.status(HttpStatus.OK).body(result.toString());
			}
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
		
	}
	
}
