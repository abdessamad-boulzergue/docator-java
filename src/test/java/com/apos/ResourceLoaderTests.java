package com.apos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apos.resources.ResourceLoaderService;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
public class ResourceLoaderTests {

	@Autowired
	ResourceLoaderService resourceLoad;
	@Test
	void testResourceLoader() {
		assertDoesNotThrow(()->{
			String name = "test_write_file.txt";
			String content = "test write to this file";
			resourceLoad.writeResource(name, content);
			String fileContent = resourceLoad.readResource(name);
			assertEquals(content, fileContent);
		});
	}
	
}
