package com.natureevent.controller;

import com.natureevent.dto.EventRequestDTO;
import com.natureevent.dto.EventResponseDTO;
import com.natureevent.dto.EventSearchResponseDTO;
import com.natureevent.dto.EventShareResponseDTO;
import com.natureevent.dto.UserResponseDTO;
import com.natureevent.service.EventService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	// =========================================================
	// CREATE EVENT
	// =========================================================

	@PostMapping
	public EventResponseDTO createEvent(@Valid @RequestBody EventRequestDTO request) {

		return eventService.createEvent(request);
	}

	// =========================================================
	// GET ALL EVENTS
	// =========================================================

	@GetMapping
	public List<EventResponseDTO> getAllEvents() {

		return eventService.getAllEvents();
	}

	// =========================================================
	// GET EVENT BY ID
	// =========================================================

	@GetMapping("/{id}")
	public EventResponseDTO getEventById(@PathVariable Long id) {

		return eventService.getEventById(id);
	}

	// =========================================================
	// UPDATE EVENT
	// =========================================================

	@PutMapping("/{id}")
	public EventResponseDTO updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO request) {

		return eventService.updateEvent(id, request);
	}

	// =========================================================
	// DELETE EVENT
	// =========================================================

	@DeleteMapping("/{id}")
	public String deleteEvent(@PathVariable Long id) {

		eventService.deleteEvent(id);

		return "Event deleted successfully";
	}

	// =========================================================
	// JOIN EVENT
	// =========================================================

	@PostMapping("/{eventId}/join")
	public EventResponseDTO joinEvent(@PathVariable Long eventId) {

		return eventService.joinEvent(eventId);
	}

	// =========================================================
	// LEAVE EVENT
	// =========================================================

	@DeleteMapping("/{eventId}/leave")
	public EventResponseDTO leaveEvent(@PathVariable Long eventId) {

		return eventService.leaveEvent(eventId);
	}

	// =========================================================
	// GET EVENT PARTICIPANTS
	// =========================================================

	@GetMapping("/{eventId}/participants")
	public List<UserResponseDTO> getEventParticipants(@PathVariable Long eventId) {

		return eventService.getEventParticipants(eventId);
	}

	@PutMapping("/{eventId}/complete")
	public EventResponseDTO completeEvent(@PathVariable Long eventId) {

		return eventService.completeEvent(eventId);
	}

	@PutMapping("/{eventId}/cancel")
	public EventResponseDTO cancelEvent(@PathVariable Long eventId) {

		return eventService.cancelEvent(eventId);
	}

	@GetMapping("/my-created")
	public List<EventResponseDTO> getMyCreatedEvents() {

		return eventService.getMyCreatedEvents();
	}

	@GetMapping("/my-joined")
	public List<EventResponseDTO> getMyJoinedEvents() {

		return eventService.getMyJoinedEvents();
	}

	// ============================================================
	// GENERATE EVENT SHARE LINK
	// ============================================================

	@GetMapping({ "/{eventId}/share", "/{eventId}/share-url" })
	public EventShareResponseDTO getEventShareUrl(@PathVariable Long eventId) {

		return eventService.getEventShareUrl(eventId);
	}

	// ============================================================
	// OPEN SHARED EVENT
	// ============================================================

	@GetMapping("/share/{eventId}")
	public EventResponseDTO getSharedEvent(@PathVariable Long eventId) {

		return eventService.getSharedEvent(eventId);
	}

	// =========================================================
	// SEARCH & FILTER EVENTS
	// =========================================================

	@GetMapping("/search")
	public EventSearchResponseDTO searchEvents(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String eventType, @RequestParam(required = false) String location,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "eventDate") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		java.time.LocalDate parsedStartDate = startDate != null ? java.time.LocalDate.parse(startDate) : null;

		java.time.LocalDate parsedEndDate = endDate != null ? java.time.LocalDate.parse(endDate) : null;

		return eventService.searchEvents(keyword, eventType, location, parsedStartDate, parsedEndDate, page, size,
				sortBy, sortDirection);
	}
}