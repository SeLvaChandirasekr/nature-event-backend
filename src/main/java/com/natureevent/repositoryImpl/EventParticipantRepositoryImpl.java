package com.natureevent.repositoryImpl;

import com.natureevent.entity.EventParticipant;
import com.natureevent.repository.EventParticipantRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class EventParticipantRepositoryImpl implements EventParticipantRepository {

	@PersistenceContext
	private EntityManager entityManager;

	// =========================================================
	// SAVE
	// =========================================================

	@Override
	@Transactional
	public EventParticipant save(EventParticipant participant) {

		if (participant.getId() == null) {

			entityManager.persist(participant);

			return participant;

		} else {

			return entityManager.merge(participant);
		}
	}

	// =========================================================
	// FIND BY EVENT ID AND USER ID
	// =========================================================

	@Override
	public Optional<EventParticipant> findByEventIdAndUserId(Long eventId, Long userId) {

		List<EventParticipant> results = entityManager
				.createQuery("SELECT ep FROM EventParticipant ep " + "WHERE ep.eventId = :eventId "
						+ "AND ep.userId = :userId", EventParticipant.class)
				.setParameter("eventId", eventId).setParameter("userId", userId).getResultList();

		return results.stream().findFirst();
	}

	// =========================================================
	// EXISTS BY EVENT ID AND USER ID
	// =========================================================

	@Override
	public boolean existsByEventIdAndUserId(Long eventId, Long userId) {

		Long count = entityManager
				.createQuery("SELECT COUNT(ep) " + "FROM EventParticipant ep " + "WHERE ep.eventId = :eventId "
						+ "AND ep.userId = :userId", Long.class)
				.setParameter("eventId", eventId).setParameter("userId", userId).getSingleResult();

		return count > 0;
	}

	// =========================================================
	// FIND ALL PARTICIPANTS FOR EVENT
	// =========================================================

	@Override
	public List<EventParticipant> findByEventId(Long eventId) {

		return entityManager
				.createQuery("SELECT ep FROM EventParticipant ep " + "WHERE ep.eventId = :eventId "
						+ "ORDER BY ep.joinedAt ASC", EventParticipant.class)
				.setParameter("eventId", eventId).getResultList();
	}

	// =========================================================
	// COUNT PARTICIPANTS
	// =========================================================

	@Override
	public long countByEventId(Long eventId) {

		return entityManager
				.createQuery("SELECT COUNT(ep) " + "FROM EventParticipant ep " + "WHERE ep.eventId = :eventId",
						Long.class)
				.setParameter("eventId", eventId).getSingleResult();
	}

	// =========================================================
	// DELETE PARTICIPANT
	// =========================================================

	@Override
	@Transactional
	public void delete(EventParticipant participant) {

		EventParticipant managedParticipant = entityManager.contains(participant) ? participant
				: entityManager.merge(participant);

		entityManager.remove(managedParticipant);
	}
}