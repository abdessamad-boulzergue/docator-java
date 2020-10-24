package com.apos.rest.controllers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apos.models.Resource;
import com.apos.models.ResourceVersion;
import com.apos.rest.repo.ResourceRepo;
import com.apos.rest.repo.VersionRepo;

@Service
public class ResourcesService {

	@Autowired
	ResourceRepo resourceRepo;
	
	@Autowired
	VersionRepo versionRepo;
	
	public Resource saveResource(Resource resource) {
		
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

	public Resource get(Long id) {
		
		return resourceRepo.getOne(id);
	}
	
}
