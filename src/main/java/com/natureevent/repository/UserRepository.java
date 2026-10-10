package com.natureevent.repository;

import com.natureevent.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	User save(User user);

	List<User> findAll();

	Optional<User> findByUsername(String username);

	Optional<User> findById(Long id);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}