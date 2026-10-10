package com.natureevent.serviceImpl;

import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.entity.User;
import com.natureevent.exception.ConflictException;
import com.natureevent.repository.UserRepository;
import com.natureevent.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// =========================================================
	// CREATE USER / REGISTER
	// =========================================================

	@Override
	public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

		// 1. Check username
		if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {

			throw new ConflictException("Username already exists: " + userRequestDTO.getUsername());
		}

		// 2. Check email
		boolean emailExists = userRepository.findAll().stream().anyMatch(
				user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(userRequestDTO.getEmail()));

		if (emailExists) {

			throw new ConflictException("Email already exists: " + userRequestDTO.getEmail());
		}

		// 3. Create User entity
		User user = new User();

		user.setName(userRequestDTO.getName());
		user.setUsername(userRequestDTO.getUsername());
		user.setEmail(userRequestDTO.getEmail());

		// 4. Encrypt password using BCrypt
		String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());

		user.setPassword(encodedPassword);

		// 5. Optional fields
		user.setCity(userRequestDTO.getCity());
		user.setBio(userRequestDTO.getBio());

		// 6. Save user
		User savedUser = userRepository.save(user);

		// 7. Convert Entity → Response DTO
		return convertToResponseDTO(savedUser);
	}

	// =========================================================
	// GET ALL USERS
	// =========================================================

	@Override
	public List<UserResponseDTO> getAllUsers() {

		return userRepository.findAll().stream().map(this::convertToResponseDTO).toList();
	}

	// =========================================================
	// ENTITY → RESPONSE DTO
	// =========================================================

	private UserResponseDTO convertToResponseDTO(User user) {

		return new UserResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getCity(),
				user.getBio());
	}
}