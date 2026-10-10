package com.natureevent.repositoryImpl;

import com.natureevent.entity.Notification;
import com.natureevent.repository.NotificationRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

	@PersistenceContext
	private EntityManager entityManager;

	// =========================================================
	// SAVE
	// =========================================================

	@Override
	@Transactional
	public Notification save(Notification notification) {

		if (notification.getId() == null) {

			entityManager.persist(notification);

			return notification;

		} else {

			return entityManager.merge(notification);
		}
	}

	// =========================================================
	// FIND BY ID
	// =========================================================

	@Override
	public Optional<Notification> findById(Long id) {

		return Optional.ofNullable(
				entityManager.find(Notification.class, id));
	}

	// =========================================================
	// FIND ALL BY USER ID
	// =========================================================

	@Override
	public List<Notification> findByUserId(Long userId) {

		return entityManager
				.createQuery(
						"SELECT n FROM Notification n "
								+ "WHERE n.userId = :userId "
								+ "ORDER BY n.createdAt DESC",
						Notification.class)
				.setParameter("userId", userId)
				.getResultList();
	}

	// =========================================================
	// FIND UNREAD BY USER ID
	// =========================================================

	@Override
	public List<Notification> findUnreadByUserId(Long userId) {

		return entityManager
				.createQuery(
						"SELECT n FROM Notification n "
								+ "WHERE n.userId = :userId "
								+ "AND n.read = false "
								+ "ORDER BY n.createdAt DESC",
						Notification.class)
				.setParameter("userId", userId)
				.getResultList();
	}

	// =========================================================
	// COUNT UNREAD BY USER ID
	// =========================================================

	@Override
	public long countUnreadByUserId(Long userId) {

		return entityManager
				.createQuery(
						"SELECT COUNT(n) FROM Notification n "
								+ "WHERE n.userId = :userId "
								+ "AND n.read = false",
						Long.class)
				.setParameter("userId", userId)
				.getSingleResult();
	}
}