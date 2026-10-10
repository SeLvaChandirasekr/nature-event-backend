package com.natureevent.repositoryImpl;

import com.natureevent.entity.EventImage;
import com.natureevent.repository.EventImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventImageRepositoryImpl implements EventImageRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EventImage save(EventImage eventImage) {

		if (eventImage.getId() == null) {
			entityManager.persist(eventImage);
			return eventImage;
		}

		return entityManager.merge(eventImage);
	}

	@Override
	public Optional<EventImage> findById(Long id) {

		EventImage eventImage = entityManager.find(EventImage.class, id);

		return Optional.ofNullable(eventImage);
	}

	@Override
	public List<EventImage> findByEventId(Long eventId) {

		String jpql = "SELECT ei FROM EventImage ei " + "WHERE ei.eventId = :eventId " + "ORDER BY ei.uploadedAt DESC";

		TypedQuery<EventImage> query = entityManager.createQuery(jpql, EventImage.class);

		query.setParameter("eventId", eventId);

		return query.getResultList();
	}

	@Override
	public void deleteById(Long id) {

		EventImage eventImage = entityManager.find(EventImage.class, id);

		if (eventImage != null) {
			entityManager.remove(eventImage);
		}
	}

	@Override
	public boolean existsById(Long id) {

		EventImage eventImage = entityManager.find(EventImage.class, id);

		return eventImage != null;
	}
}