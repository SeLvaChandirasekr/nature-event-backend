package com.natureevent.serviceImpl;

import com.natureevent.dto.LoginRequestDTO;
import com.natureevent.dto.LoginResponseDTO;
import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.entity.User;
import com.natureevent.exception.ConflictException;
import com.natureevent.exception.UnauthorizedException;
import com.natureevent.repository.UserRepository;
import com.natureevent.security.JwtService;
import com.natureevent.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	// =========================================================
	// LOGIN
	// =========================================================

	@Override
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

		// 1. Find user by username
		User user = userRepository.findByUsername(loginRequestDTO.getUsername())
				.orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

		// 2. Check password
		boolean passwordMatches = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

		if (!passwordMatches) {
			throw new UnauthorizedException("Invalid username or password");
		}

		// 3. Generate JWT
		String token = jwtService.generateToken(user.getUsername());

		// 4. Return JWT
		return new LoginResponseDTO(token);
	}

	// =========================================================
	// REGISTER
	// =========================================================

	@Override
	public UserResponseDTO register(UserRequestDTO request) {

		// 1. Check username already exists
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {

			throw new ConflictException("Username already exists: " + request.getUsername());
		}

		// 2. Check email already exists
		boolean emailExists = userRepository.findAll().stream()
				.anyMatch(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(request.getEmail()));

		if (emailExists) {

			throw new ConflictException("Email already exists: " + request.getEmail());
		}

		// 3. Create new User
		User user = new User();

		user.setName(request.getName());
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());

		// 4. Encrypt password
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		user.setPassword(encodedPassword);

		// 5. Optional fields
		user.setCity(request.getCity());
		user.setBio(request.getBio());

		// 6. Save user
		User savedUser = userRepository.save(user);

		// 7. Convert Entity to Response DTO
		return convertToResponseDTO(savedUser);
	}

	// =========================================================
	// ENTITY -> RESPONSE DTO
	// =========================================================

	private UserResponseDTO convertToResponseDTO(User user) {

		UserResponseDTO response = new UserResponseDTO();

		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setCity(user.getCity());
		response.setBio(user.getBio());

		return response;
	}
}