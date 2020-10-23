package com.apos;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.apos.plugins.GeneralConfigs;
import com.fasterxml.jackson.core.JsonProcessingException;
@ComponentScan
@SpringBootApplication
public class AposApplication implements ApplicationRunner	 {
	@Autowired
	GeneralConfigs config;
	public static void main(String[] args) {
		SpringApplication.run(AposApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args) {
		try {
			System.out.println(config.getPluginDatasource());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Application started with option names : "+ args.getOptionNames());
		System.out.println("Application started with non option args : "+ args.getNonOptionArgs());
		
		
	}

}
