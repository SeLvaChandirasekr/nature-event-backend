package com.natureevent.repositoryImpl;

import com.natureevent.entity.EventInvitation;
import com.natureevent.repository.EventInvitationRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class EventInvitationRepositoryImpl implements EventInvitationRepository {

	@PersistenceContext
	private EntityManager entityManager;

	// =========================================================
	// SAVE
	// =========================================================

	@Override
	@Transactional
	public EventInvitation save(EventInvitation invitation) {

		if (invitation.getId() == null) {

			// New invitation
			entityManager.persist(invitation);

			return invitation;

		} else {

			// Existing invitation
			return entityManager.merge(invitation);
		}
	}

	// =========================================================
	// FIND BY ID
	// =========================================================

	@Override
	public Optional<EventInvitation> findById(Long id) {

		return Optional.ofNullable(entityManager.find(EventInvitation.class, id));
	}

	// =========================================================
	// FIND BY INVITEE ID
	// =========================================================

	@Override
	public List<EventInvitation> findByInviteeId(Long inviteeId) {

		return entityManager
				.createQuery("SELECT ei FROM EventInvitation ei " + "WHERE ei.inviteeId = :inviteeId "
						+ "ORDER BY ei.invitedAt DESC", EventInvitation.class)
				.setParameter("inviteeId", inviteeId).getResultList();
	}

	// =========================================================
	// FIND BY INVITER ID
	// =========================================================

	@Override
	public List<EventInvitation> findByInviterId(Long inviterId) {

		return entityManager
				.createQuery("SELECT ei FROM EventInvitation ei " + "WHERE ei.inviterId = :inviterId "
						+ "ORDER BY ei.invitedAt DESC", EventInvitation.class)
				.setParameter("inviterId", inviterId).getResultList();
	}

	// =========================================================
	// CHECK DUPLICATE INVITATION
	// =========================================================

	@Override
	public boolean existsByEventIdAndInviterIdAndInviteeId(Long eventId, Long inviterId, Long inviteeId) {

		Long count = entityManager
				.createQuery("SELECT COUNT(ei) " + "FROM EventInvitation ei " + "WHERE ei.eventId = :eventId "
						+ "AND ei.inviterId = :inviterId " + "AND ei.inviteeId = :inviteeId "
						+ "AND ei.status = :status", Long.class)
				.setParameter("eventId", eventId).setParameter("inviterId", inviterId)
				.setParameter("inviteeId", inviteeId).setParameter("status", "PENDING").getSingleResult();

		return count > 0;
	}

	// =========================================================
	// NEW - FIND BY EVENT ID
	// =========================================================

	@Override
	public List<EventInvitation> findByEventId(Long eventId) {

		return entityManager
				.createQuery("SELECT ei FROM EventInvitation ei " + "WHERE ei.eventId = :eventId "
						+ "ORDER BY ei.invitedAt DESC", EventInvitation.class)
				.setParameter("eventId", eventId).getResultList();
	}

	// =========================================================
	// NEW - FIND BY EVENT ID AND INVITEE ID
	// =========================================================

	@Override
	public Optional<EventInvitation> findByEventIdAndInviteeId(Long eventId, Long inviteeId) {

		List<EventInvitation> result = entityManager
				.createQuery(
						"SELECT ei FROM EventInvitation ei " + "WHERE ei.eventId = :eventId "
								+ "AND ei.inviteeId = :inviteeId " + "ORDER BY ei.invitedAt DESC",
						EventInvitation.class)
				.setParameter("eventId", eventId).setParameter("inviteeId", inviteeId).setMaxResults(1).getResultList();

		return result.stream().findFirst();
	}

	// =========================================================
	// NEW - CHECK PENDING INVITATION
	// =========================================================

	@Override
	public boolean existsByEventIdAndInviteeId(Long eventId, Long inviteeId) {

		Long count = entityManager
				.createQuery("SELECT COUNT(ei) " + "FROM EventInvitation ei " + "WHERE ei.eventId = :eventId "
						+ "AND ei.inviteeId = :inviteeId " + "AND ei.status = :status", Long.class)
				.setParameter("eventId", eventId).setParameter("inviteeId", inviteeId).setParameter("status", "PENDING")
				.getSingleResult();

		return count > 0;
	}
}
