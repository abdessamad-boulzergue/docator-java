package com.apos.dataloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.json.JSONObject;

public class DataFileWriter<T> implements DataWriter<T>{

	private BufferedWriter wirter;
	public DataFileWriter(String fileName) throws Exception{
		 File file = new File(fileName);
		 if(!file.exists()  && !file.createNewFile()) {
			 throw new Exception("Can not create File  : "+fileName);
		 }
		 this.wirter = new BufferedWriter( new FileWriter(fileName) );
		
	}
	@Override
	public void write(Data<T> data) throws Exception {
		
		    if(data.getValue() instanceof String)
		    	this.wirter.write(data.getValue().toString());
		    else if(data.getValue() instanceof JSONObject)
		    	this.wirter.write(((JSONObject)data.getValue()).toString(4));
		    
		
	}

}
