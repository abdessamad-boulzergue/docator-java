package com.apos;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.AccessException;

import com.apos.plugins.GeneralConfigs;
import com.apos.wizard.ActionDefinition;
import com.apos.wizard.WizardActionManager;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AposApplication.class
		)
public class ActionDefinitionTest {

	@Autowired
	WizardActionManager manager;
	@Autowired
	GeneralConfigs conf;
	
	@Test
	void initActionDefinition() throws AccessException {
		manager.initActionDefinitions();
		ActionDefinition initAction = manager.getAction("INIT");
		assertNotNull(initAction);
		HashMap params = new HashMap<>();
		params.put("home", conf.getResourcesPath());
		params.put("savedTemplate", "C:/HCS/e-Access/home/temp/1910012967_1611319162302.template");
			initAction.execute(params);

		
		HashMap<String, ActionDefinition> actions = manager.getActionList();
		assertNotNull(actions);
		assertNotEquals(0, actions.size());
		actions.keySet().forEach(key->{
			assertNotNull(actions.get(key));
			assertNotNull(actions.get(key).getKey());
		});

	}
}
