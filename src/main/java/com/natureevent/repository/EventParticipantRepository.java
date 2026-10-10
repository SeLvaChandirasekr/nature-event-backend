package com.natureevent.repository;

import com.natureevent.entity.EventParticipant;

import java.util.List;
import java.util.Optional;

public interface EventParticipantRepository {

	EventParticipant save(EventParticipant participant);

	Optional<EventParticipant> findByEventIdAndUserId(Long eventId, Long userId);

	boolean existsByEventIdAndUserId(Long eventId, Long userId);

	List<EventParticipant> findByEventId(Long eventId);

	long countByEventId(Long eventId);

	void delete(EventParticipant participant);
}