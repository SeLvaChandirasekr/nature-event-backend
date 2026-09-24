package com.natureevent.repository;

import com.natureevent.entity.User;

import java.util.List;

public interface UserRepository {

	User save(User user);

	List<User> findAll();
}