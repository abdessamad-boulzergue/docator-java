package com.apos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.apos.models.User;
@ComponentScan
@SpringBootApplication
public class AposApplication implements CommandLineRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(AposApplication.class, args);
	}
	
	@Override
	public void run(String...str ) {

		jdbcTemplate.query("SELECT ID, NAME,password from Users", new Object[] {},
		        (rs, rowNum) -> { 
		        	User usr = new User(rs.getLong(1),rs.getString(2),rs.getString(3));
		        	
		        	return usr;
		        }
		    ).forEach(user -> System.out.println(user.toString()));
		
	}

}
