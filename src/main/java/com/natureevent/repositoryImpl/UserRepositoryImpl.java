package com.natureevent.repositoryImpl;

import com.natureevent.entity.User;
import com.natureevent.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	// =========================================================
	// SAVE
	// =========================================================

	@Override
	@Transactional
	public User save(User user) {

		if (user.getId() == null) {
			entityManager.persist(user);
			return user;
		}

		return entityManager.merge(user);
	}

	// =========================================================
	// FIND ALL
	// =========================================================

	@Override
	public List<User> findAll() {

		return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
	}

	// =========================================================
	// FIND BY USERNAME
	// =========================================================

	@Override
	public Optional<User> findByUsername(String username) {

		List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", username).getResultList();

		return users.stream().findFirst();
	}

	// =========================================================
	// FIND BY ID
	// =========================================================

	@Override
	public Optional<User> findById(Long id) {

		return Optional.ofNullable(entityManager.find(User.class, id));
	}

	// =========================================================
	// EXISTS BY USERNAME
	// =========================================================

	@Override
	public boolean existsByUsername(String username) {

		Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
				.setParameter("username", username).getSingleResult();

		return count > 0;
	}

	// =========================================================
	// EXISTS BY EMAIL
	// =========================================================

	@Override
	public boolean existsByEmail(String email) {

		Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
				.setParameter("email", email).getSingleResult();

		return count > 0;
	}
}