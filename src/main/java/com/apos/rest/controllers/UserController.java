package com.apos.rest.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apos.models.User;
import com.apos.rest.repo.UserRepo;


@RestController
@RequestMapping(path="/users")
@CrossOrigin
public class UserController {

	
	private final UserRepo repo;
	
	public UserController(UserRepo repo) {
		this.repo =repo;
	}
	
	@GetMapping("/{user_id}")
	User getUser(@PathVariable(name = "user_id") String user_id) {
		return new User();
	}
	
	@GetMapping({"/all",""})
	List<User> getUsers(){
		return repo.findAll();
	}
}
