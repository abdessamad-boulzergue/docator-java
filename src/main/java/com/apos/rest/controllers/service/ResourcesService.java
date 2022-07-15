package com.apos.rest.controllers.service;



import java.io.IOException;

import java.util.Date;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.stereotype.Service;



import com.apos.models.Resource;

import com.apos.models.ResourceType;

import com.apos.models.ResourceVersion;

import com.apos.resources.IResource;

import com.apos.resources.ResourceStorageService;

import com.apos.rest.exceptions.ResourceNotFoundException;

import com.apos.rest.repo.ResourceRepo;

import com.apos.rest.repo.ResourceTypeRepo;

import com.apos.rest.repo.VersionRepo;



@Service

public class ResourcesService {



	private static final long SIZE = 1024l;



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



	public void writeResourceTofile(String fileName, String content)  {

        IResource document = new com.apos.resources.Resource(fileName,fileName, SIZE, new Date());

		 resourceLoader.saveResource(document, content.getBytes());

	}



	public String readResourceFromFile(String fileName) {

		IResource resource= resourceLoader.getResource(fileName);

		String result = null;

		if(resource !=null)

			result= new String(resource.getContent());

		else

			throw new ResourceNotFoundException(fileName);

		return result;

	}



	public void delete(long id) {

		try {

			resourceRepo.deleteById(id);

			resourceLoader.deleteResource(String.valueOf(id));

		} catch (EmptyResultDataAccessException   e) {

			throw new ResourceNotFoundException(id);

		}

	}



	



	public String readResourceFromJsonFile(String id) {

		return readResourceFromFile(id.concat(".json"));

	}



	public String getResourcePath(String id) {

		try {

			return resourceLoader.getResource(id).toFile().getAbsolutePath();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;

	}

	

}

