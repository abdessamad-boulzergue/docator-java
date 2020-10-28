package com.apos.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.apos.plugins.GeneralConfigs;
import com.apos.rest.exceptions.ResourceNotFoundException;

@Service
public class ResourceLoaderService  {
	
	Logger logger = LoggerFactory.getLogger(ResourceLoaderService.class);
	
	@Autowired
	GeneralConfigs configs;
	private static final String DEFAULT_ENCODE="utf-8";
	

   public String readResource(String name) {
	     File file = new File(configs.getResourcesPath(), name);
		return this.readAll(file, DEFAULT_ENCODE);
   }
   
   public String readResource(String name,String encode) throws IOException {
	   File file = new File(configs.getResourcesPath(), name);
	   return this.readAll(file, encode);
   }
   
	private String readAll(File file, String encode)    {
		
		StringBuilder resultBuilder = new StringBuilder();

		try {
			
			if( !file.exists()) {
				logger.error("access failed, file : {} , exception : {}", file.getAbsolutePath() ,"resource not found");
				throw new ResourceNotFoundException(file.getName());
			}
			
			try (
					InputStream in = new FileInputStream(file);
					BufferedReader  reader = new BufferedReader(new InputStreamReader(in, encode));
				){
				reader.lines().forEach(resultBuilder::append);
			}
			
		} catch (IOException e) {
			logger.error("access failed, file : {} , exception : {}", file.getAbsolutePath() , e.getMessage());
			throw new ResourceAccessException("could not access file : "+file.getName());
		}
		
		
		return resultBuilder.toString();
	}
	   public String writeResource(String name,String content) throws IOException {
		   return write(name, content);
	   }
	   private String write(String fileName, String content) throws IOException  {
		   return write(fileName,content,DEFAULT_ENCODE);
	   }
	   private String write(String fileName, String content, String encode) throws IOException  {
	    	File file = new File(configs.getResourcesPath(), fileName);
	    	return write(file,content,encode);
	    }
	   
	    private String write(File file, String content, String encode) throws IOException  {
		
		OutputStream out = new FileOutputStream(file);
	
		try(
				BufferedWriter  writer = new BufferedWriter(new OutputStreamWriter(out,encode))
			) {
			
			writer.write(content);
		}
		return file.getAbsolutePath();
	}
}
