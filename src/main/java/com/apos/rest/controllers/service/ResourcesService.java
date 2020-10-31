package com.apos.rest.controllers.service;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.apos.models.Resource;
import com.apos.models.ResourceType;
import com.apos.models.ResourceVersion;
import com.apos.resources.ResourceLoaderService;
import com.apos.rest.exceptions.ResourceNotFoundException;
import com.apos.rest.repo.ResourceRepo;
import com.apos.rest.repo.ResourceTypeRepo;
import com.apos.rest.repo.VersionRepo;

@Service
public class ResourcesService {

	@Autowired
	   ResourceLoaderService resourceLoader;
	
	@Autowired
	ResourceRepo resourceRepo;
	
	@Autowired
	VersionRepo versionRepo;
	
	@Autowired
	ResourceTypeRepo typesRepo;
	
	public Resource saveResource(Resource resource) {
		
		if(resource.getType().getId() == null) {
			ResourceType type = typesRepo.get(resource.getType().getName());
			resource.setType(type);
		}
		Resource savedResource = resourceRepo.save(resource);
		
		ResourceVersion currentVersion = new ResourceVersion();
		ResourceVersion topVersion  = versionRepo.getResourceMaxVersion(savedResource.getId());
		if(topVersion!=null) {
			currentVersion.setName(topVersion.getNextVersionName());
		}
		currentVersion.setResource(savedResource);
		versionRepo.save(currentVersion);
		savedResource.setMaxVersion(currentVersion);
		resourceRepo.save(savedResource);
		 
		return savedResource;
	}
	
	public List<ResourceVersion> getResourceVersion(Long resourceId){
		return versionRepo.getResourceVersions(resourceId);
	}
	
	public ResourceVersion getResourceMaxVersion(Long resourceId){
		return versionRepo.getResourceMaxVersion(resourceId);
	}

	public ResourceType saveType(ResourceType type)  {
		if(type!=null && type.hasName()) {
			return typesRepo.save(type);
		}else
		  throw new IllegalArgumentException("Invalide Type");
	}
	public Resource get(Long id) {
		
		Resource resource =  resourceRepo.findById(id).orElse(null);
		if(resource ==null) {
			throw new ResourceNotFoundException(id);
		}
		return resource;
	}

	public ResourceType getType(long id) {
		ResourceType type =  typesRepo.findById(id).orElse(null);
		if(type ==null) {
			throw new ResourceNotFoundException("ResourceType" ,id);
		}
		return type;
	}

	public List<ResourceType> getTypes() {
		return typesRepo.findAll();
	}

	public List<Resource> getResources() {
		return resourceRepo.findAll();
	}

	public ResourceType getType(String typeName) {
		ResourceType type = typesRepo.get(typeName);
		
		return type;
	}

	public String writeResourceTofile(String fileName, String content)  {
		
		return resourceLoader.writeResource(fileName, content);
	}

	public String readResourceFromFile(String fileName) {
		return resourceLoader.readResource(fileName);
	}
	
}
