package com.apos.plugins;

import java.util.HashMap;
import java.util.Map;

abstract public class EnginesScriptlet {

	  public final static char SCRIPTLET   = 'S';
	  public final static char ENVIRONMENT = 'E';
	  public final static char VARIABLE    = 'V';
	  public final static char OUTPUT      = 'O';
	  EngineVariables variables = new EngineVariables();
	private boolean inited=false;
	public void init() {
	    try {
	    	if(!inited) {
		      initArgs(SCRIPTLET, new HashMap<>());
		      initArgs(ENVIRONMENT, new HashMap<>());
		      initArgs(VARIABLE, new HashMap<>());
		      initArgs(OUTPUT, new HashMap<>());
		      inited = true;
	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	  }
	public void initArgs(char type, Map<String, String> properties) throws Exception {
		variables.initArgs(type, properties);
	  }

	  public Map<String, String> getArgs(char type) throws Exception {
	    return variables.getArgs(type);
	  }
	abstract public void run();
	public  void clear() {
		variables.clear();
	}
}
