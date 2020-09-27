package com.apos;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan
@SpringBootApplication
public class AposApplication implements ApplicationRunner	 {

	public static void main(String[] args) {
		SpringApplication.run(AposApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args) {
		
		System.out.println("Application started with option names : "+ args.getOptionNames());
		System.out.println("Application started with non option args : "+ args.getNonOptionArgs());

		
	}

}
