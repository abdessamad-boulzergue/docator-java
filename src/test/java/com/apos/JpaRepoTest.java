package com.apos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.apos.models.Resource;
import com.apos.models.ResourceType;
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
		final ResourceType type1 = Resource.getResourceType(ResourceType.TYPE_PLUGIN);
		
			List<ResourceType> types = resourceService.getTypes().stream().filter((savedType)->{
				return savedType.getName().equals(type1.getName());
			}).collect(Collectors.toList());
			
				if(types.isEmpty()) {
					assertDoesNotThrow(()->{
						 resourceService.saveType(type1);
					});
				}else {
				assertThrows(DataIntegrityViolationException.class, ()->{
					 resourceService.saveType(type1);
				});
			}
			 
		final ResourceType type = resourceService.getTypes().stream().limit(1).findFirst().orElse(null);
		assertNotNull(type);
		Resource resource = new Resource();
		resource.setName("resource testResourceService");
		resource.setDescription("description fro resource testResourceService");
		
		resource.setType(type );
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
	
	
}
