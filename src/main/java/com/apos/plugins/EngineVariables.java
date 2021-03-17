package com.apos.plugins;

import java.util.HashMap;
import java.util.Map;

public class EngineVariables {

	private Map<String, Object> _template;
	private Map<String, Object> _environment;
	private Map<String, Object> _variables;
	private Map<String, Object> _output;

	public void initArgs(char type, Map<String, Object> properties) throws Exception {
	    switch (type) {
	      case EnginesScriptlet.SCRIPTLET:
	        _template = properties;
	        break;
	      case EnginesScriptlet.ENVIRONMENT:
	        _environment = properties;
	        break;
	      case EnginesScriptlet.VARIABLE:
	        _variables = properties;
	        break;
	      case EnginesScriptlet.OUTPUT:
	        _output = properties;
	        break;

	      default:
	        throw new Exception("invalid Engine property type : " + type);
	    }
	  }

	public Map<String, Object> getArgs(char type) throws Exception {
		switch (type) {
	      case EnginesScriptlet.SCRIPTLET:
	        return _template ;
	      case EnginesScriptlet.ENVIRONMENT:
	    	  return _environment ;
	      case EnginesScriptlet.VARIABLE:
	    	  return _variables ;
	      case EnginesScriptlet.OUTPUT:
	    	  return _output ;

	      default:
	        throw new Exception("invalid Engine property type : " + type);
	    }
	}
	public void setValue(char type,String key,Object value) throws Exception {
		switch (type) {
		case EnginesScriptlet.SCRIPTLET:
			 _template.put(key, value) ;break;
		case EnginesScriptlet.ENVIRONMENT:
			 _environment.put(key, value) ;break;
		case EnginesScriptlet.VARIABLE:
			 _variables.put(key, value) ;break;
		case EnginesScriptlet.OUTPUT:
			 _output.put(key, value) ;break;
			
		default:
			throw new Exception("invalid Engine property type : " + type);
		}
	}

	public void clear() {
		_template.clear();
		_template= new HashMap<>();
		_environment.clear();
		_environment= new HashMap<>();
		_variables.clear();
		_variables= new HashMap<>();
		_output.clear();
		_output= new HashMap<>();
	}

	public boolean containstKey(char type, String key) {
		switch (type) {
		case EnginesScriptlet.SCRIPTLET:
			 return _template.containsKey(key);
		case EnginesScriptlet.ENVIRONMENT:
			return _environment.containsKey(key);
		case EnginesScriptlet.VARIABLE:
			return _variables.containsKey(key);
		case EnginesScriptlet.OUTPUT:
			return _output.containsKey(key);
			
		default:
			return false;
		}
	}
}
