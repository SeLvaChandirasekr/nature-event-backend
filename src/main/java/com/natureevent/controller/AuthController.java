package com.natureevent.controller;

import com.natureevent.dto.LoginRequestDTO;
import com.natureevent.dto.LoginResponseDTO;
import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// =========================================================
	// REGISTER
	// =========================================================

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDTO register(@Valid @RequestBody UserRequestDTO request) {

		return authService.register(request);
	}

	// =========================================================
	// LOGIN
	// =========================================================

	@PostMapping("/login")
	public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {

		return authService.login(request);
	}
}