package com.apos;

import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
	void loadUsersNotAuthenticated() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/users/all")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(MockMvcResultMatchers.status().is(401))
			      .andExpect(MockMvcResultMatchers.content().string(""));
			      
	}

}
