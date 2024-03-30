package com.example.demo.controller;

import com.example.demo.model.CreateUserRequest;
import com.example.demo.model.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
		userService.registerUser(createUserRequest);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<UserResponse> getUserById(Authentication authentication) {
		UserResponse userResponse = userService.getUserByName(authentication.getName());

		return ResponseEntity.ok(userResponse);
	}

}
