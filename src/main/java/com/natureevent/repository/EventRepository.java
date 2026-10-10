package com.natureevent.repository;

import com.natureevent.entity.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

	Event save(Event event);

	List<Event> findAll();

	Optional<Event> findById(Long id);

	void deleteById(Long id);

	boolean existsById(Long id);

	List<Event> findByCreatedBy(Long userId);

	List<Event> findJoinedEventsByUserId(Long userId);

	List<Event> searchEvents(String keyword, String eventType, String location, LocalDate startDate, LocalDate endDate,
			int page, int size, String sortBy, String sortDirection);

	long countSearchEvents(String keyword, String eventType, String location, LocalDate startDate, LocalDate endDate);
}