package com.apos.plugins;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.apos.socket.ClientSessionException;

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
	public void initArgs(char type, Map<String, Object> properties) throws Exception {
		variables.initArgs(type, properties);
	  }

	  public Map<String, Object> getArgs(char type) throws Exception {
	    return variables.getArgs(type);
	  }
	  public abstract void run() throws ClientSessionException;
	public  void clear() {
		variables.clear();
	}
	
	public void setScriptletArgs(String key,String value,boolean override) throws Exception {
		if(override || this.containsScriptletKey(key))
			this.variables.setValue(SCRIPTLET, key, value);
	}
	public void setOutputArgs(String key,Object value) throws Exception {
		this.variables.setValue(OUTPUT, key, value);
	}
	public Map<String, Object> getScripletArgs() throws Exception {
		return this.getArgs(SCRIPTLET);
	}

	public Object getScripletArg(String key) throws Exception {
		return this.getArgs(SCRIPTLET).get(key);
	}
	public Object getOutputArg(String key) throws Exception {
		return this.getArgs(OUTPUT).get(key);
	}
	public Object getVariableArg(String key) throws Exception {
		return this.getArgs(VARIABLE).get(key);
	}
	public Object getEnvironmentArg(String key) throws Exception {
		return this.getArgs(ENVIRONMENT).get(key);
	}
	public Map<String, Object> getOutputArgs() throws Exception {
		return this.getArgs(OUTPUT);
	}
	public boolean containsScriptletKey(String key) {
		return this.variables.containstKey(SCRIPTLET,key);
	}
	public boolean containsVariableKey(String key) {
		return this.variables.containstKey(VARIABLE,key);
	}
	public boolean containsOutputKey(String key) {
		return this.variables.containstKey(OUTPUT,key);
	}
	public boolean containsEnvironmentKey(String key) {
		return this.variables.containstKey(ENVIRONMENT,key);

	}
	public void setArgs(char typeVars, String key, String value) throws Exception {
		variables.setValue(typeVars, key, value);
	}
}
