package com.natureevent.controller;

import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// =========================================================
	// CREATE USER
	// =========================================================

	@PostMapping
	public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request) {

		return userService.createUser(request);
	}

	// =========================================================
	// GET ALL USERS
	// =========================================================

	@GetMapping
	public List<UserResponseDTO> getAllUsers() {

		return userService.getAllUsers();
	}
}