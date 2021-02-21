package com.apos.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apos.plugins.GeneralConfigs;

@Service
public class WizardService  {
	
	Logger logger = LoggerFactory.getLogger(WizardService.class);
	
	@Autowired
	   ResourceLoaderService resourceLoader;
	
	@Autowired
	GeneralConfigs configs;
	
	public String getDefFile() {
		return resourceLoader.getActionsDefinitionPath();
	}
}
