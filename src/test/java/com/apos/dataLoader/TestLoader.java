package com.apos.dataLoader;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.apos.dataloader.DataFileReader;
import com.apos.dataloader.DataFileWriter;
import com.apos.dataloader.JSONDataReader;
import com.apos.dataloader.LoaderProcess;
import com.apos.dataloader.StringDataReader;
import com.apos.dataloader.StringToJsonFormat;

public class TestLoader {

	@Test
	public void testStringSrc() throws Exception{
		StringDataReader strSource = new StringDataReader("hello");
		LoaderProcess<String, String> loader = new LoaderProcess<>();
		loader.setInput(strSource);
		loader.process();
	}
	
	@Test
	public void testJSONSrc() throws Exception{
		JSONObject hello = new JSONObject();
		hello.put("hel", 12);
		JSONDataReader strSource = new JSONDataReader(hello );
		LoaderProcess<JSONObject, String> loader = new LoaderProcess<>();
		loader.setInput(strSource);
		loader.process();
	}
	
	@Test
	public void testStringToJsonFormater() throws Exception {
		
		
		LoaderProcess<String, JSONObject> loader = new LoaderProcess<>();
		
		DataFileReader reader = new DataFileReader("C:\\HCS\\Designer\\home\\temp\\Rules.json");
		DataFileWriter<JSONObject> writer = new DataFileWriter<>("C:\\HCS\\Designer\\home\\temp\\Rules2.json");
		
		loader.setInput(reader);
		loader.setWriter(writer);
		loader.setFormatter(new StringToJsonFormat());
		loader.process();
		
	}
	
}
