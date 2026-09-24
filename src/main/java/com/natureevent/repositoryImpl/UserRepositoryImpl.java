package com.natureevent.repositoryImpl;

import com.natureevent.entity.User;
import com.natureevent.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public User save(User user) {

		entityManager.persist(user);

		return user;
	}

	@Override
	public List<User> findAll() {

		return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
	}
}