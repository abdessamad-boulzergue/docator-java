package com.apos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.apos.models.Resource;
import com.apos.models.ResourceType;
import com.apos.rest.controllers.service.ResourcesService;
@ComponentScan
@SpringBootApplication
public class AposApplication implements ApplicationRunner	 {
	
	@Autowired
	ResourcesService resourceService;
	
	public static void main(String[] args) {
		SpringApplication.run(AposApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args)  {
		
		if(resourceService.getType(ResourceType.TYPE_WORKFLOW) == null) {
			resourceService.saveType(Resource.getResourceType(ResourceType.TYPE_WORKFLOW));
		}
		if(resourceService.getType(ResourceType.TYPE_PLUGIN) == null) {
			resourceService.saveType(Resource.getResourceType(ResourceType.TYPE_PLUGIN));
		}
		
		System.out.println("Application started with option names : "+ args.getOptionNames());
		System.out.println("Application started with non option args : "+ args.getNonOptionArgs());
		
		
	}

}
