package com.natureevent.service;

import com.natureevent.dto.EventRequestDTO;
import com.natureevent.dto.EventResponseDTO;
import com.natureevent.dto.EventSearchResponseDTO;
import com.natureevent.dto.EventShareResponseDTO;
import com.natureevent.dto.UserResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

	EventResponseDTO createEvent(EventRequestDTO request);

	List<EventResponseDTO> getAllEvents();

	EventResponseDTO getEventById(Long eventId);

	EventResponseDTO updateEvent(Long eventId, EventRequestDTO request);

	void deleteEvent(Long eventId);

	EventResponseDTO joinEvent(Long eventId);

	EventResponseDTO leaveEvent(Long eventId);

	List<UserResponseDTO> getEventParticipants(Long eventId);

	EventResponseDTO completeEvent(Long eventId);

	EventResponseDTO cancelEvent(Long eventId);

	List<EventResponseDTO> getMyCreatedEvents();

	List<EventResponseDTO> getMyJoinedEvents();

	EventShareResponseDTO getEventShareUrl(Long eventId);

	EventResponseDTO getSharedEvent(Long eventId);

	// Module 11 - Event Search & Filter
	EventSearchResponseDTO searchEvents(String keyword, String eventType, String location, LocalDate startDate,
			LocalDate endDate, int page, int size, String sortBy, String sortDirection);
}