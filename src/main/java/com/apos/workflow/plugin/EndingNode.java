package com.apos.workflow.plugin;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import com.apos.plugins.EnginesScriptlet;
import com.apos.plugins.IPlugin;
import com.apos.workflow.model.Application;

public class EndingNode extends GenericPlugin{
	  private final static String START_ICON = "images/start-workflow.png";
	    private  EnginesScriptlet implementation;

	@Override
	public String getName() {
		return "StartNode";
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
		return false;
	}

	@Override
	public EnginesScriptlet getImplementation() {
		if(implementation==null) {
			implementation = new EndingScriptlet();
		}
		implementation.init();
		return implementation;
	}
}
