package com.apos.workflow.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apos.plugins.EnginesScriptlet;
import com.apos.utils.XProperties;

public class JobTicketData {

	  private Object  synchroObject = new Object();
	private String dataFilePath;
	  private XProperties params             = new XProperties();
	  private static String _OUTPUT_="OUTPUT.";

	  
	public void load(File jobTicketDataFile) {
		synchronized(synchroObject) {
		      dataFilePath = jobTicketDataFile.getAbsolutePath();
		   try (FileInputStream inStr = new FileInputStream(jobTicketDataFile) ) {
				params.load(inStr);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void loadParameters(Map<String, String> params) {

	    for (String key : params.keySet())
	    {
	      this.params.setProperty(key, params.get(key));      
	    }
	  
	}


	public Map<String,String> getRunningEnvironment() {
		return new HashMap(params);
	   
	}


	public void setData(String key, String value) {
		synchronized(params) {
			params.setProperty(key, value);
		}
	}


	public void populateWithScripletOuput(Map<String, Object> outputArgs) {
		if(outputArgs!=null && outputArgs.size()>0) {
			Iterator<String> keys = outputArgs.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				if(outputArgs.get(key)!=null) {
					this.params.put(_OUTPUT_.concat(key), outputArgs.get(key));
				}
			}
		}
	}


	public void updateScriplet(EnginesScriptlet wfScriptlet) {

		substitutionCombinedParameters(wfScriptlet, params);
	}


	private void substitutionCombinedParameters(EnginesScriptlet scriptlet, XProperties properties) {
		try {
			
			substitutionCombinedParameters( null,scriptlet, EnginesScriptlet.ENVIRONMENT, properties);
		      substitutionCombinedParameters(null,scriptlet, EnginesScriptlet.VARIABLE, properties);
		      substitutionCombinedParameters(null,scriptlet, EnginesScriptlet.SCRIPTLET, properties);
		      substitutionCombinedParameters( null,scriptlet, EnginesScriptlet.OUTPUT, properties);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	      
	}


	private void substitutionCombinedParameters(HashMap<String, Object> argsParam,EnginesScriptlet scriplet, char typeVars,XProperties properties) throws Exception {
	    
		Map<String, Object> args = (scriplet==null)? argsParam : scriplet.getArgs(typeVars);
	  
	    if(args!=null && args.size()>0) {
	    	synchronized(args) {
	    		Pattern pattern = Pattern.compile("\\{\\w+\\}");
	    		Iterator<String> keysIter = args.keySet().iterator();
	    		while(keysIter.hasNext()) {
	    			String key = keysIter.next();
	    			Object objValue = args.get(key);
	    			if(objValue instanceof String) {
	    				String value = (String) objValue;
	    				Matcher match = pattern.matcher(value);	
	    				if(match.find()) {
	    					String matchedValue = constructString(scriplet, properties, pattern, value);
	    					args.put(key, matchedValue);
	    					if(matchedValue!=null && !matchedValue.isEmpty() && scriplet!=null) {
	    						scriplet.setArgs(typeVars,key,matchedValue);
	    					}
	    				}

	    			}
	    		}
	    	}
	    }
	    	
	}
	String constructString(EnginesScriptlet scriplet ,XProperties properties,Pattern pattern, String value) throws Exception {
		Matcher match = pattern.matcher(value);
		String result =value;
		while(match.find()) {
			String matched = value.substring(match.start()+1,match.end()-1);
			String matchedValue = getMatchValue(scriplet,matched,properties);
			matchedValue = constructString(scriplet, properties, pattern, matchedValue);
			if(matchedValue!=null) {
				result = result.replaceAll("\\{" + matched + "\\}", Matcher.quoteReplacement(matchedValue));
			}
		}
		return result;
	}
	 private String getMatchValue(EnginesScriptlet scriptlet, String matchFound, XProperties params) throws Exception {
		    String matchValue = null;
		    if ((scriptlet != null) && scriptlet.containsEnvironmentKey(matchFound) ) {
		      matchValue = scriptlet.getEnvironmentArg(matchFound).toString();
		    } else if ((scriptlet != null) && scriptlet.containsOutputKey(matchFound)) {
		      matchValue = scriptlet.getOutputArg(matchFound).toString();
		    } else if ((scriptlet != null) && scriptlet.containsVariableKey(matchFound)) {
		      matchValue = scriptlet.getVariableArg(matchFound).toString();
		    } else if ((scriptlet != null) && scriptlet.containsScriptletKey(matchFound) ) {
		      matchValue = scriptlet.getScripletArg(matchFound).toString();
		    } else if (params.containsKey(matchFound)) {
		      matchValue = params.get(matchFound).toString();
		    } else {
		      System.out.println(getClass() + "Undefined Variable " + matchFound);
		    }
		    return matchValue;
		  }
}
