package com.natureevent.repositoryImpl;

import com.natureevent.entity.Event;
import com.natureevent.repository.EventRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepositoryImpl implements EventRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Event save(Event event) {

		if (event.getId() == null) {

			// New event
			entityManager.persist(event);

			return event;

		} else {

			// Existing event
			return entityManager.merge(event);
		}
	}

	@Override
	public List<Event> findAll() {

		return entityManager.createQuery("SELECT e FROM Event e", Event.class).getResultList();
	}

	@Override
	public Optional<Event> findById(Long id) {

		return Optional.ofNullable(entityManager.find(Event.class, id));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		Event event = entityManager.find(Event.class, id);

		if (event != null) {
			entityManager.remove(event);
		}
	}

	@Override
	public boolean existsById(Long id) {

		return entityManager.find(Event.class, id) != null;
	}

	@Override
	public List<Event> findByCreatedBy(Long userId) {

		return entityManager
				.createQuery("SELECT e FROM Event e " + "WHERE e.createdBy = :userId "
						+ "ORDER BY e.eventDate ASC, e.eventTime ASC", Event.class)
				.setParameter("userId", userId).getResultList();
	}

	@Override
	public List<Event> findJoinedEventsByUserId(Long userId) {

		return entityManager
				.createQuery(
						"SELECT e FROM Event e " + "WHERE e.id IN (" + "SELECT ep.eventId FROM EventParticipant ep "
								+ "WHERE ep.userId = :userId" + ") " + "ORDER BY e.eventDate ASC, e.eventTime ASC",
						Event.class)
				.setParameter("userId", userId).getResultList();
	}

	// Module 11 - Event Search & Filter
	@Override
	public List<Event> searchEvents(String keyword, String eventType, String location, LocalDate startDate,
			LocalDate endDate, int page, int size, String sortBy, String sortDirection) {

		StringBuilder jpql = new StringBuilder("SELECT e FROM Event e WHERE 1=1");

		// Keyword search
		if (keyword != null && !keyword.trim().isEmpty()) {
			jpql.append(" AND (LOWER(e.title) LIKE LOWER(:keyword) " + "OR LOWER(e.description) LIKE LOWER(:keyword))");
		}

		// Event type filter
		if (eventType != null && !eventType.trim().isEmpty()) {
			jpql.append(" AND LOWER(e.eventType) = LOWER(:eventType)");
		}

		// Location filter
		if (location != null && !location.trim().isEmpty()) {
			jpql.append(" AND LOWER(e.location) LIKE LOWER(:location)");
		}

		// Start date filter
		if (startDate != null) {
			jpql.append(" AND e.eventDate >= :startDate");
		}

		// End date filter
		if (endDate != null) {
			jpql.append(" AND e.eventDate <= :endDate");
		}

		// Sorting
		String validSortField = "eventDate";

		if ("title".equalsIgnoreCase(sortBy)) {
			validSortField = "title";
		} else if ("eventDate".equalsIgnoreCase(sortBy)) {
			validSortField = "eventDate";
		} else if ("eventTime".equalsIgnoreCase(sortBy)) {
			validSortField = "eventTime";
		} else if ("createdAt".equalsIgnoreCase(sortBy)) {
			validSortField = "createdAt";
		}

		String direction = "DESC";

		if ("asc".equalsIgnoreCase(sortDirection)) {
			direction = "ASC";
		}

		jpql.append(" ORDER BY e." + validSortField + " " + direction);

		TypedQuery<Event> query = entityManager.createQuery(jpql.toString(), Event.class);

		// Set parameters
		if (keyword != null && !keyword.trim().isEmpty()) {
			query.setParameter("keyword", "%" + keyword.trim() + "%");
		}

		if (eventType != null && !eventType.trim().isEmpty()) {
			query.setParameter("eventType", eventType.trim());
		}

		if (location != null && !location.trim().isEmpty()) {
			query.setParameter("location", "%" + location.trim() + "%");
		}

		if (startDate != null) {
			query.setParameter("startDate", startDate);
		}

		if (endDate != null) {
			query.setParameter("endDate", endDate);
		}

		// Pagination
		int safePage = Math.max(page, 0);
		int safeSize = Math.max(size, 1);

		query.setFirstResult(safePage * safeSize);
		query.setMaxResults(safeSize);

		return query.getResultList();
	}

	@Override
	public long countSearchEvents(String keyword, String eventType, String location, LocalDate startDate,
			LocalDate endDate) {

		StringBuilder jpql = new StringBuilder("SELECT COUNT(e) FROM Event e WHERE 1=1");

		// Keyword search
		if (keyword != null && !keyword.trim().isEmpty()) {
			jpql.append(" AND (LOWER(e.title) LIKE LOWER(:keyword) " + "OR LOWER(e.description) LIKE LOWER(:keyword))");
		}

		// Event type filter
		if (eventType != null && !eventType.trim().isEmpty()) {
			jpql.append(" AND LOWER(e.eventType) = LOWER(:eventType)");
		}

		// Location filter
		if (location != null && !location.trim().isEmpty()) {
			jpql.append(" AND LOWER(e.location) LIKE LOWER(:location)");
		}

		// Start date filter
		if (startDate != null) {
			jpql.append(" AND e.eventDate >= :startDate");
		}

		// End date filter
		if (endDate != null) {
			jpql.append(" AND e.eventDate <= :endDate");
		}

		TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);

		// Set parameters
		if (keyword != null && !keyword.trim().isEmpty()) {
			query.setParameter("keyword", "%" + keyword.trim() + "%");
		}

		if (eventType != null && !eventType.trim().isEmpty()) {
			query.setParameter("eventType", eventType.trim());
		}

		if (location != null && !location.trim().isEmpty()) {
			query.setParameter("location", "%" + location.trim() + "%");
		}

		if (startDate != null) {
			query.setParameter("startDate", startDate);
		}

		if (endDate != null) {
			query.setParameter("endDate", endDate);
		}

		return query.getSingleResult();
	}
}