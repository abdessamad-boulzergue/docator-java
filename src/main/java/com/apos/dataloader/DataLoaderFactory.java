package com.apos.dataloader;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.json.JSONObject;

public class DataLoaderFactory {

	private static LoaderProcess loader;
	
	public static LoaderProcess getLoader(Class in, Class out,HashMap<String, Object> args) throws Exception {
		
		DataReader reader=null;
		DataWriter writer = null;

		if(StringDataReader.class.equals(in))
			reader = new StringDataReader((String) args.get("stringData"));
		else if(JSONDataReader.class.equals(in))
			reader = new JSONDataReader((JSONObject) args.get("jsonData"));
		else if(DataFileReader.class.equals(in))
			reader = new DataFileReader((String) args.get("inputFile"));
		
		
		
		if(DataFileWriter.class.equals(out))
			writer = new DataFileWriter((String) args.get("outputFile"));
		
		loader = new LoaderProcess();
		loader.setInput(reader);
		loader.setWriter(writer);
		
		return loader;
	}
	
}
