package com.ahom.forgetpassword.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahom.forgetpassword.entity.User;
import com.ahom.forgetpassword.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/saveuser")
	public ResponseEntity<User> SaveUsr(@RequestBody User user) {
		userService.saveUser(user);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email) {
		String response = userService.forgotPassword(email);
		if (!response.startsWith("Invalid")) {
			response = "http://localhost:8080/reset-password?token=" + response;
		}
		return response;
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password) {
		return userService.resetPassword(token, password);
	}

}
