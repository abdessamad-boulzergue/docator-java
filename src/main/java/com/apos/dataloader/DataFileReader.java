package com.apos.dataloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DataFileReader implements DataReader<String>{

		private BufferedReader breader;

		public DataFileReader(String fileName) throws FileNotFoundException {
			FileReader reader = new FileReader(fileName);
			 breader = new BufferedReader(reader);
		}
	
	@Override
	public Data<String> read() throws Exception {
	        
			String line="";
			String result="";
		while( (line =breader.readLine()) != null) {
			result+=line;
		}
		
		return new Data<String>(result);
	}
}
