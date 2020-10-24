package com.apos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apos.models.Resource;
import com.apos.models.ResourceVersion;
import com.apos.rest.controllers.service.ResourcesService;
import com.apos.rest.repo.ResourceRepo;
import com.apos.rest.repo.VersionRepo;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
public class JpaRepoTest {

	@Autowired
	ResourceRepo resourceRepo;
	
	@Autowired
	ResourcesService resourceService;
	@Autowired
	VersionRepo versionRepo;
	
	String versionName = ResourceVersion.DEFAULT_VERSION_NAME;
	@Test
	public void testResourceService() {
		
		Resource resource = new Resource();
		resource.setName("resource testResourceService");
		resource.setDescription("description fro resource testResourceService");
		Resource savedResource = resourceService.saveResource(resource);
	    assertEquals(ResourceVersion.DEFAULT_VERSION_NAME,savedResource.getMaxVersion().getName());
		assertEquals(true, savedResource.getId()>0);
		
		savedResource.setDescription("updated testResourceService: "+savedResource.getId());
		savedResource = resourceService.saveResource(savedResource);
		assertEquals(ResourceVersion.getNextVersionName(ResourceVersion.DEFAULT_VERSION_NAME),savedResource.getMaxVersion().getName());
		
	    ResourceVersion maxversion = resourceService.getResourceMaxVersion(savedResource.getId());
	    assertNotNull(maxversion);
	    assertEquals(savedResource.getMaxVersion().getId(), maxversion.getId());
		List<ResourceVersion> versions = resourceService.getResourceVersion(savedResource.getId());
		assertNotNull(versions);
		assertNotEquals(0, versions.size());
		versions.stream().forEach(version->{
			assertEquals(versionName,version.getName());
			versionName = ResourceVersion.getNextVersionName(versionName);
		});

	}
	
	@Test
	public void testResourceRepo() {
		
		
		Resource resource = new Resource();
		resource.setName("resource 1");
		resource.setDescription("description resource 1");
		Resource savedResource = resourceRepo.save(resource );
		assertEquals(true, savedResource.getId()>0);
		
		ResourceVersion version = new ResourceVersion();
		version.setResource(savedResource);
		ResourceVersion savedVersion = versionRepo.save(version);
		assertEquals(true, savedVersion.getId()>0);
		
		version = new ResourceVersion();
		version.setResource(savedResource);
		 savedVersion = versionRepo.save(version);
		assertEquals(true, savedVersion.getId()>0);
		
		savedResource.setMaxVersion(version);
		resourceRepo.save(savedResource);
		
	}
	
}
