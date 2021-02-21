package com.apos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apos.workflow.runtime.WorkflowAgent;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
public class WorkflowRunTest {
	@Autowired
	WorkflowAgent agent;
	
	@Test
	void runWorkflow(){

		 String xpdl="D:\\sefas\\master\\apos\\src\\main\\resources\\47";
		String jobTicketData="C:/hcs/env/test/client_4.1/tagTest2_1.1/config/1142.prop";
		Map<String, String> mapParameters = new HashMap<String, String>();
		try {
			agent.startWorkflow("1", xpdl, jobTicketData, mapParameters );
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}
	
	}
}
