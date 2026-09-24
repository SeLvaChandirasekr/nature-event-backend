package com.natureevent.service;

import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

	UserResponseDTO createUser(UserRequestDTO userRequestDTO);

	List<UserResponseDTO> getAllUsers();
}