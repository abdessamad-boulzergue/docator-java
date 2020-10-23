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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apos.plugins.GeneralConfigs;

@Service
public class ResourceLoaderService  {
	
	@Autowired
	GeneralConfigs configs;
	private static final String DEFAULT_ENCODE="utf-8";
	

   public String readResource(String name) throws IOException {
	     File file = new File(configs.getResourcesPath(), name);
		return this.readAll(file, DEFAULT_ENCODE);
   }
   
   public String readResource(String name,String encode) throws IOException {
	   File file = new File(configs.getResourcesPath(), name);
	   return this.readAll(file, encode);
   }
   
	private String readAll(File file, String encode) throws IOException {
		
		StringBuilder resultBuilder = new StringBuilder();
		InputStream in = new FileInputStream(file);
	
		try(
				BufferedReader  reader = new BufferedReader(new InputStreamReader(in, encode))
			) {
			
			reader.lines().forEach(resultBuilder::append);
			
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
