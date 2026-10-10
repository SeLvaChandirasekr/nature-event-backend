package com.natureevent.repository;

import com.natureevent.entity.EventInvitation;

import java.util.List;
import java.util.Optional;

public interface EventInvitationRepository {

	EventInvitation save(EventInvitation invitation);

	Optional<EventInvitation> findById(Long id);

	List<EventInvitation> findByInviteeId(Long inviteeId);

	List<EventInvitation> findByInviterId(Long inviterId);

	boolean existsByEventIdAndInviterIdAndInviteeId(Long eventId, Long inviterId, Long inviteeId);

	List<EventInvitation> findByEventId(Long eventId);

	Optional<EventInvitation> findByEventIdAndInviteeId(Long eventId, Long inviteeId);

	boolean existsByEventIdAndInviteeId(Long eventId, Long inviteeId);
}