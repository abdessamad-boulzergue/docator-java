package com.apos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.apos.rest.repo.UserRepo;



@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
@AutoConfigureMockMvc
class AposApplicationTests {

	@Autowired
    private MockMvc mvc;
 
    @Autowired
    private UserRepo repository;
    
    @Test
	void authenticate() throws Exception {
	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("username", "root");
		params.add("password", "hN6+dG1yxksTzWzrui8JAA==");
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login").params(params )
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(MockMvcResultMatchers.status().isOk())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.notNullValue()))
			      .andReturn();
		//.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.startsWith("{\"token\":\"")));
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

}
