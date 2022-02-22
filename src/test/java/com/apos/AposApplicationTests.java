package com.apos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Iterator;

import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.apos.plugins.IPluginLoad;
import com.apos.resources.service.ResourceLoaderService;
import com.apos.rest.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
@AutoConfigureMockMvc
class AposApplicationTests {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	IPluginLoad _ploader;
	@Autowired
	ResourceLoaderService resourceLoad;
 
    @Autowired
    private UserRepo repository;
    
    @Autowired
	@Qualifier("PluginDatasource")
	JsonNode pluginDatasource;
    
    @Test
	void authenticate() throws Exception {
	

		String content = "{\"username\": \"root\", \"password\" : \"hN6+dG1yxksTzWzrui8JAA==\" }";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login").content(content )
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(MockMvcResultMatchers.status().isOk())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.notNullValue()))
			      .andReturn();
		JSONObject jsonResult = new org.json.JSONObject(result.getResponse().getContentAsString());
		String[] tokenParts = jsonResult.getString("token").split("\\.");
		assertNotEquals(tokenParts, null);
		assertEquals(tokenParts.length, 3);
		
	}
    
	@Test
	void loadUsersNotAuthenticated() throws Exception {
	
		mvc.perform(MockMvcRequestBuilders.get("/users/all")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(MockMvcResultMatchers.status().is(401))
			      .andExpect(MockMvcResultMatchers.content().string(""));
			      
	}

	@Test 
	void testGeneralConfig() {
	
			assertEquals(true,pluginDatasource.has("sources"));
			
			JsonNode sources = pluginDatasource.get("sources");
			Iterator<JsonNode> sourceIter = sources.elements();
			sourceIter.forEachRemaining(src->{
				
				assertEquals(true, src.has("type"));
				assertEquals(true, src.has("id"));
				assertEquals(false, src.get("id").asText().isEmpty());
				assertEquals(false, src.get("type").asText().isEmpty());
				if(src.get("type").asText().equals("socket")) {
					assertEquals(true, src.has("address"));
					assertEquals(true, src.has("port"));
					assertDoesNotThrow(()->{
						Integer.valueOf(src.get("port").asText());
					});
				}
				
			});
			
			
		
	}
	
	@Test
	void testSocketPluginLoaderConnection() {
		assertDoesNotThrow(()->{
			_ploader.load();
		});
	}
	
}
