package com.apos.rest.controllers;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apos.models.Resource;
import com.apos.models.ResourceType;
import com.apos.rest.controllers.service.ResourcesService;

@RestController
@RequestMapping("/resource")
@CrossOrigin
public class ResourcesController {

	@Autowired
	ResourcesService resourceService;
	
	@GetMapping("/type")
	public ResponseEntity<Object> getType(@RequestParam("id") String id){
		
		try {
			ResourceType resource = resourceService.getType(Long.parseLong(id));
			return ResponseEntity.status(HttpStatus.OK).body(resource);

		} catch (NumberFormatException  e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}catch (EntityNotFoundException   e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Resource not found with id : "+id);
		}
	}
	@GetMapping("/types")
	public ResponseEntity<Object> getTypes(){
		
		try {
			List<ResourceType> resource = resourceService.getTypes();
			return ResponseEntity.status(HttpStatus.OK).body(resource);
			
		} catch (Exception  e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	@GetMapping("/all")
	public ResponseEntity<Object> getResources(){
		
		try {
			List<Resource> resources = resourceService.getResources();
			return ResponseEntity.status(HttpStatus.OK).body(resources);
			
		} catch (Exception  e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<Object> getResource(@RequestParam("id") String id){
			
			try {
				Resource resource = resourceService.get(Long.parseLong(id));
				return ResponseEntity.status(HttpStatus.OK).body(resource);
				
			} catch (NumberFormatException  e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}catch (EntityNotFoundException   e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Resource not found with id : "+id);
			}		
	   }
	
}
