package com.natureevent.serviceImpl;

import com.natureevent.dto.EventRequestDTO;
import com.natureevent.dto.EventResponseDTO;
import com.natureevent.dto.EventSearchResponseDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.entity.Event;
import com.natureevent.entity.EventParticipant;
import com.natureevent.entity.User;
import com.natureevent.exception.ConflictException;
import com.natureevent.exception.ForbiddenException;
import com.natureevent.exception.ResourceNotFoundException;
import com.natureevent.repository.EventParticipantRepository;
import com.natureevent.repository.EventRepository;
import com.natureevent.repository.UserRepository;
import com.natureevent.service.EventService;
import com.natureevent.dto.EventShareResponseDTO;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final EventParticipantRepository eventParticipantRepository;
	private final UserRepository userRepository;

	@Value("${app.base-url:http://localhost:8080}")
	private String baseUrl;

	public EventServiceImpl(EventRepository eventRepository, EventParticipantRepository eventParticipantRepository,
			UserRepository userRepository) {

		this.eventRepository = eventRepository;
		this.eventParticipantRepository = eventParticipantRepository;
		this.userRepository = userRepository;
	}

	// =========================================================
	// CREATE EVENT
	// =========================================================

	@Override
	@Transactional
	public EventResponseDTO createEvent(EventRequestDTO request) {

		User currentUser = getCurrentUser();

		Event event = new Event();

		event.setTitle(request.getTitle());
		event.setDescription(request.getDescription());
		event.setEventType(request.getEventType());
		event.setLocation(request.getLocation());
		event.setEventDate(request.getEventDate());
		event.setEventTime(request.getEventTime());
		event.setMaxParticipants(request.getMaxParticipants());

		event.setCreatedBy(currentUser.getId());

		event.setParticipantCount(0);
		event.setStatus("ACTIVE");

		event.setCreatedAt(LocalDateTime.now());
		event.setUpdatedAt(LocalDateTime.now());

		Event savedEvent = eventRepository.save(event);

		return convertToResponseDTO(savedEvent);
	}

	// =========================================================
	// GET ALL EVENTS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<EventResponseDTO> getAllEvents() {

		return eventRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	// =========================================================
	// GET EVENT BY ID
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public EventResponseDTO getEventById(Long eventId) {

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		return convertToResponseDTO(event);
	}

	// =========================================================
	// UPDATE EVENT
	// =========================================================

	@Override
	@Transactional
	public EventResponseDTO updateEvent(Long eventId, EventRequestDTO request) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		// Only event creator can update
		if (!event.getCreatedBy().equals(currentUser.getId())) {

			throw new ForbiddenException("You are not allowed to update this event");
		}

		// Completed event cannot be updated
		if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Completed event cannot be updated");
		}

		// Cancelled event cannot be updated
		if ("CANCELLED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Cancelled event cannot be updated");
		}

		event.setTitle(request.getTitle());
		event.setDescription(request.getDescription());
		event.setEventType(request.getEventType());
		event.setLocation(request.getLocation());
		event.setEventDate(request.getEventDate());
		event.setEventTime(request.getEventTime());
		event.setMaxParticipants(request.getMaxParticipants());

		event.setUpdatedAt(LocalDateTime.now());

		Event updatedEvent = eventRepository.save(event);

		return convertToResponseDTO(updatedEvent);
	}

	// =========================================================
	// DELETE EVENT
	// =========================================================

	@Override
	@Transactional
	public void deleteEvent(Long eventId) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		// Only creator can delete
		if (!event.getCreatedBy().equals(currentUser.getId())) {

			throw new ForbiddenException("You are not allowed to delete this event");
		}

		// Completed event cannot be deleted
		if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Completed event cannot be deleted");
		}

		eventRepository.deleteById(eventId);
	}

	// =========================================================
	// JOIN EVENT
	// =========================================================

	@Override
	@Transactional
	public EventResponseDTO joinEvent(Long eventId) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		// Check event status
		if (!"ACTIVE".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("You cannot join an event that is " + event.getStatus().toLowerCase());
		}

		// Check whether user already joined
		if (eventParticipantRepository.findByEventIdAndUserId(eventId, currentUser.getId()).isPresent()) {

			throw new ConflictException("You have already joined this event");
		}

		// Check maximum participants
		long currentParticipants = eventParticipantRepository.countByEventId(eventId);

		if (event.getMaxParticipants() != null && currentParticipants >= event.getMaxParticipants()) {

			throw new ConflictException("Event has reached maximum participants");
		}

		// Create participant
		EventParticipant participant = new EventParticipant();

		participant.setEventId(eventId);
		participant.setUserId(currentUser.getId());
		participant.setJoinedAt(LocalDateTime.now());

		eventParticipantRepository.save(participant);

		// Update participant count
		event.setParticipantCount((int) eventParticipantRepository.countByEventId(eventId));

		event.setUpdatedAt(LocalDateTime.now());

		Event updatedEvent = eventRepository.save(event);

		return convertToResponseDTO(updatedEvent);
	}

	// =========================================================
	// LEAVE EVENT
	// =========================================================

	@Override
	@Transactional
	public EventResponseDTO leaveEvent(Long eventId) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		EventParticipant participant = eventParticipantRepository.findByEventIdAndUserId(eventId, currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException("You have not joined this event"));

		eventParticipantRepository.delete(participant);

		// Update participant count
		event.setParticipantCount((int) eventParticipantRepository.countByEventId(eventId));

		event.setUpdatedAt(LocalDateTime.now());

		Event updatedEvent = eventRepository.save(event);

		return convertToResponseDTO(updatedEvent);
	}

	// =========================================================
	// GET EVENT PARTICIPANTS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<UserResponseDTO> getEventParticipants(Long eventId) {

		// Check whether event exists
		if (!eventRepository.existsById(eventId)) {

			throw new ResourceNotFoundException("Event not found with id: " + eventId);
		}

		// Get all participants
		List<EventParticipant> participants = eventParticipantRepository.findByEventId(eventId);

		/*
		 * EventParticipant ↓ userId ↓ Optional<User> ↓ User ↓ UserResponseDTO
		 */

		return participants.stream().map(EventParticipant::getUserId).map(userId -> userRepository.findById(userId))
				.filter(java.util.Optional::isPresent).map(java.util.Optional::get).map(this::convertUserToResponseDTO)
				.collect(Collectors.toList());
	}

	// =========================================================
	// COMPLETE EVENT
	// =========================================================

	@Override
	@Transactional
	public EventResponseDTO completeEvent(Long eventId) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		// Only creator can complete
		if (!event.getCreatedBy().equals(currentUser.getId())) {

			throw new ForbiddenException("Only event creator can complete the event");
		}

		if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Event is already completed");
		}

		if ("CANCELLED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Cancelled event cannot be completed");
		}

		event.setStatus("COMPLETED");
		event.setUpdatedAt(LocalDateTime.now());

		Event completedEvent = eventRepository.save(event);

		return convertToResponseDTO(completedEvent);
	}

	// =========================================================
	// CANCEL EVENT
	// =========================================================

	@Override
	@Transactional
	public EventResponseDTO cancelEvent(Long eventId) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		// Only creator can cancel
		if (!event.getCreatedBy().equals(currentUser.getId())) {

			throw new ForbiddenException("Only event creator can cancel the event");
		}

		if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Completed event cannot be cancelled");
		}

		if ("CANCELLED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Event is already cancelled");
		}

		event.setStatus("CANCELLED");
		event.setUpdatedAt(LocalDateTime.now());

		Event cancelledEvent = eventRepository.save(event);

		return convertToResponseDTO(cancelledEvent);
	}

	// =========================================================
	// GET MY CREATED EVENTS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<EventResponseDTO> getMyCreatedEvents() {

		User currentUser = getCurrentUser();

		return eventRepository.findByCreatedBy(currentUser.getId()).stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	// =========================================================
	// GET MY JOINED EVENTS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<EventResponseDTO> getMyJoinedEvents() {

		User currentUser = getCurrentUser();

		return eventRepository.findJoinedEventsByUserId(currentUser.getId()).stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	// =========================================================
	// GET CURRENT LOGGED-IN USER
	// =========================================================

	private User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {

			throw new ForbiddenException("User is not authenticated");
		}

		String username = authentication.getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

		// IMPORTANT: return the user
		return user;
	}

	// =========================================================
	// EVENT -> RESPONSE DTO
	// =========================================================

	private EventResponseDTO convertToResponseDTO(Event event) {

		int participantCount = (int) eventParticipantRepository.countByEventId(event.getId());

		event.setParticipantCount(participantCount);

		String shareLink = "/api/events/" + event.getId();

		return new EventResponseDTO(event.getId(), event.getTitle(), event.getDescription(), event.getEventType(),
				event.getLocation(), event.getEventDate(), event.getEventTime(), event.getMaxParticipants(),
				event.getCreatedBy(), participantCount, event.getStatus(), shareLink, event.getCreatedAt(),
				event.getUpdatedAt());
	}

	// =========================================================
	// USER -> RESPONSE DTO
	// =========================================================

	private UserResponseDTO convertUserToResponseDTO(User user) {

		UserResponseDTO response = new UserResponseDTO();

		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setCity(user.getCity());
		response.setBio(user.getBio());

		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public EventShareResponseDTO getEventShareUrl(Long eventId) {

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		String shareUrl = baseUrl + "/api/events/share/" + event.getId();

		return new EventShareResponseDTO(event.getId(), event.getTitle(), shareUrl);
	}

	@Override
	@Transactional(readOnly = true)
	public EventResponseDTO getSharedEvent(Long eventId) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
		return convertToResponseDTO(event);
	}

	// =========================================================
	// SEARCH & FILTER EVENTS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public EventSearchResponseDTO searchEvents(String keyword, String eventType, String location, LocalDate startDate,
			LocalDate endDate, int page, int size, String sortBy, String sortDirection) {

		// Prevent invalid pagination values
		int safePage = Math.max(page, 0);
		int safeSize = Math.max(size, 1);

		// Search events
		List<Event> events = eventRepository.searchEvents(keyword, eventType, location, startDate, endDate, safePage,
				safeSize, sortBy, sortDirection);

		// Count total matching events
		long totalElements = eventRepository.countSearchEvents(keyword, eventType, location, startDate, endDate);

		// Calculate total pages
		int totalPages = (int) Math.ceil((double) totalElements / safeSize);

		// Convert Event -> EventResponseDTO
		List<EventResponseDTO> eventResponses = events.stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());

		return new EventSearchResponseDTO(eventResponses, safePage, safeSize, totalElements, totalPages);
	}

}