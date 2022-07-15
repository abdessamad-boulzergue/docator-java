package com.apos.rest.controllers;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apos.models.Resource;
import com.apos.models.ResourceType;
import com.apos.rest.controllers.service.ResourcesService;
import com.apos.rest.exceptions.DataFormatException;
import com.apos.rest.validator.AposValidator;
import com.apos.rest.validator.ValidatorFactory;

@RestController
@RequestMapping("/resource")
@CrossOrigin
public class ResourcesController {

	@Autowired
	ResourcesService resourceService;
	
	ValidatorFactory validatorFactory = new  ValidatorFactory();
	
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
	@DeleteMapping(path = "{id}")
	public ResponseEntity<Object> deleteResource(@PathVariable("id") String id) {
		
			try {
				resourceService.delete(Long.parseLong(id));
			} catch (NumberFormatException e) {
				throw  DataFormatException.getException(id, Long.class);
			}
			return ResponseEntity.status(HttpStatus.OK).body("");

	}
	@GetMapping
	public ResponseEntity<Object> getResource(@RequestParam("id") String id){
			
			try {
				Resource resource = resourceService.get(Long.parseLong(id));
				return ResponseEntity.status(HttpStatus.OK).body(resource);
				
			} catch (NumberFormatException  e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}		
	   }
	
	@PostMapping
	public ResponseEntity<Object> saveResource(@RequestBody Resource resource){
		
		    @SuppressWarnings("unchecked")
			AposValidator<Resource> validator = (AposValidator<Resource>) validatorFactory.getValidator(Resource.class);
		    validator.validate(resource);
		    
			Resource savedResource = resourceService.saveResource(resource);
			return ResponseEntity.status(HttpStatus.OK).body(savedResource);
			
		
	}
	
}
