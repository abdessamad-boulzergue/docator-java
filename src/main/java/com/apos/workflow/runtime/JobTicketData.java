package com.apos.workflow.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.apos.utils.XProperties;

public class JobTicketData {

	  private Object  synchroObject = new Object();
	private String dataFilePath;
	  private XProperties params             = new XProperties();

	  
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


	public HashMap<String,String> getRunningEnvironment() {
		return new HashMap(params);
	   
	}


	public void setData(String key, String value) {
		synchronized(params) {
			params.setProperty(key, value);
		}
	}

}
