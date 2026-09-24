package com.natureevent.serviceImpl;

import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.entity.User;
import com.natureevent.repository.UserRepository;
import com.natureevent.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

		// Request DTO → Entity
		User user = new User();

		user.setName(userRequestDTO.getName());
		user.setUsername(userRequestDTO.getUsername());
		user.setEmail(userRequestDTO.getEmail());
		user.setPassword(userRequestDTO.getPassword());
		user.setCity(userRequestDTO.getCity());
		user.setBio(userRequestDTO.getBio());

		// Save Entity
		User savedUser = userRepository.save(user);

		// Entity → Response DTO
		return convertToResponseDTO(savedUser);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {

		return userRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	private UserResponseDTO convertToResponseDTO(User user) {

		return new UserResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getCity(),
				user.getBio());
	}
}