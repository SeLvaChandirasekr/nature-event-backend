package com.natureevent.service;

import com.natureevent.dto.LoginRequestDTO;
import com.natureevent.dto.LoginResponseDTO;
import com.natureevent.dto.UserRequestDTO;
import com.natureevent.dto.UserResponseDTO;

public interface AuthService {

	UserResponseDTO register(UserRequestDTO request);

	LoginResponseDTO login(LoginRequestDTO request);

}