package com.apos.rest.controllers;



import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apos.rest.controllers.service.IAuthentication;
import com.apos.rest.dto.Credentials;
import com.apos.rest.dto.JwtAuthenticationResponse;

@RestController
@RequestMapping(path = "/")
@CrossOrigin
public class AuthenticationController {
	
	@Autowired
	IAuthentication authService;
	
	
	@PostMapping(path ="login")
	public ResponseEntity login(Credentials credential){
		System.out.println(credential);
		String username = credential.getUsername();
		String password = credential.getPassword();
		String token =  authService.login(username, password);
		
		 if (token!=null){
		
		       JwtAuthenticationResponse resp = new JwtAuthenticationResponse (token);
		       return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(resp);
		     }
		 JSONObject error = new JSONObject();
	     error.put("message", "Authentication failed, please contact your admin");
	     error.put("path", "/login");
		     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.toString(1)) ; 

	}
}
