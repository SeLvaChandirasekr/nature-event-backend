package com.natureevent.repository;

import com.natureevent.entity.EventImage;

import java.util.List;
import java.util.Optional;

public interface EventImageRepository {

	EventImage save(EventImage eventImage);

	Optional<EventImage> findById(Long id);

	List<EventImage> findByEventId(Long eventId);

	void deleteById(Long id);

	boolean existsById(Long id);
}