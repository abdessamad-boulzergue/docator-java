package com.apos.workflow.plugin;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.apos.plugins.EnginesScriptlet;
import com.apos.workflow.model.Application;

public class StartingNode extends  GenericPlugin{
	  private final static String START_ICON = "images/start-workflow.png";
	    private  EnginesScriptlet implementation;
		private Application parentApplication;

	@Override
	public String getName() {
		return "EndNode";
	}

	@Override
	public Object getIcon() {
		try {
			return ImageIO.read(new File(START_ICON));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getUId() {
		return null;
	}

	@Override
	public boolean isStarting() {
		return true;
	}

	@Override
	public EnginesScriptlet getImplementation() {
		if(implementation==null) {
			implementation = new StartingScriptlet();
		}
		implementation.init();
		return implementation;
	}
}
