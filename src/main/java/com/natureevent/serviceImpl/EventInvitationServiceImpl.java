package com.natureevent.serviceImpl;

import com.natureevent.dto.EventInvitationRequestDTO;
import com.natureevent.dto.EventInvitationResponseDTO;
import com.natureevent.entity.Event;
import com.natureevent.entity.EventInvitation;
import com.natureevent.entity.EventParticipant;
import com.natureevent.entity.User;
import com.natureevent.exception.ConflictException;
import com.natureevent.exception.ForbiddenException;
import com.natureevent.exception.ResourceNotFoundException;
import com.natureevent.repository.EventInvitationRepository;
import com.natureevent.repository.EventParticipantRepository;
import com.natureevent.repository.EventRepository;
import com.natureevent.repository.UserRepository;
import com.natureevent.service.EventInvitationService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventInvitationServiceImpl implements EventInvitationService {

	private final EventInvitationRepository invitationRepository;
	private final EventRepository eventRepository;
	private final EventParticipantRepository participantRepository;
	private final UserRepository userRepository;

	public EventInvitationServiceImpl(EventInvitationRepository invitationRepository, EventRepository eventRepository,
			EventParticipantRepository participantRepository, UserRepository userRepository) {

		this.invitationRepository = invitationRepository;
		this.eventRepository = eventRepository;
		this.participantRepository = participantRepository;
		this.userRepository = userRepository;
	}

	// =========================================================
	// INVITE USER
	// =========================================================

	@Override
	@Transactional
	public EventInvitationResponseDTO inviteUser(EventInvitationRequestDTO request) {

		User currentUser = getCurrentUser();

		// 1. Find event
		Event event = eventRepository.findById(request.getEventId())
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + request.getEventId()));

		// 2. Only event creator can invite users
		if (event.getCreatedBy() == null || !event.getCreatedBy().equals(currentUser.getId())) {

			throw new ForbiddenException("Only event creator can invite users");
		}

		// 3. Check event status
		if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Completed event cannot accept invitations");
		}

		if ("CANCELLED".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("Cancelled event cannot accept invitations");
		}

		// 4. Find invited user
		User invitee = userRepository.findByUsername(request.getUsername()).orElseThrow(
				() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));

		// 5. Prevent self invitation
		if (currentUser.getId().equals(invitee.getId())) {

			throw new ConflictException("You cannot invite yourself");
		}

		// 6. Check whether already participant
		if (participantRepository.findByEventIdAndUserId(event.getId(), invitee.getId()).isPresent()) {

			throw new ConflictException("User is already a participant");
		}

		// 7. Check duplicate invitation
		boolean alreadyInvited = invitationRepository.existsByEventIdAndInviterIdAndInviteeId(event.getId(),
				currentUser.getId(), invitee.getId());

		if (alreadyInvited) {

			throw new ConflictException("User has already been invited");
		}

		// 8. Create invitation
		EventInvitation invitation = new EventInvitation();

		invitation.setEventId(event.getId());
		invitation.setInviterId(currentUser.getId());
		invitation.setInviteeId(invitee.getId());
		invitation.setStatus("PENDING");
		invitation.setInvitedAt(LocalDateTime.now());

		EventInvitation saved = invitationRepository.save(invitation);

		return convertToResponseDTO(saved);
	}

	// =========================================================
	// ACCEPT INVITATION
	// =========================================================

	@Override
	@Transactional
	public EventInvitationResponseDTO acceptInvitation(Long invitationId) {

		User currentUser = getCurrentUser();

		EventInvitation invitation = getInvitation(invitationId);

		// Only invitee can accept
		if (!invitation.getInviteeId().equals(currentUser.getId())) {

			throw new ForbiddenException("You cannot accept this invitation");
		}

		// Check invitation status
		if (!"PENDING".equalsIgnoreCase(invitation.getStatus())) {

			throw new ConflictException("Invitation is already " + invitation.getStatus().toLowerCase());
		}

		// Find event
		Event event = eventRepository.findById(invitation.getEventId()).orElseThrow(
				() -> new ResourceNotFoundException("Event not found with id: " + invitation.getEventId()));

		// Check event status
		if (!"ACTIVE".equalsIgnoreCase(event.getStatus())) {

			throw new ConflictException("This event is no longer active");
		}

		// Check maximum participants
		long currentParticipants = participantRepository.countByEventId(event.getId());

		if (event.getMaxParticipants() != null && currentParticipants >= event.getMaxParticipants()) {

			throw new ConflictException("Event has reached maximum participants");
		}

		// Check whether user already joined
		boolean alreadyParticipant = participantRepository.findByEventIdAndUserId(event.getId(), currentUser.getId())
				.isPresent();

		if (!alreadyParticipant) {

			EventParticipant participant = new EventParticipant();

			participant.setEventId(event.getId());
			participant.setUserId(currentUser.getId());
			participant.setJoinedAt(LocalDateTime.now());

			participantRepository.save(participant);

			// Update participant count
			int updatedCount = (int) participantRepository.countByEventId(event.getId());

			event.setParticipantCount(updatedCount);
			event.setUpdatedAt(LocalDateTime.now());

			eventRepository.save(event);
		}

		// Update invitation
		invitation.setStatus("ACCEPTED");
		invitation.setRespondedAt(LocalDateTime.now());

		EventInvitation updated = invitationRepository.save(invitation);

		return convertToResponseDTO(updated);
	}

	// =========================================================
	// REJECT INVITATION
	// =========================================================

	@Override
	@Transactional
	public EventInvitationResponseDTO rejectInvitation(Long invitationId) {

		User currentUser = getCurrentUser();

		EventInvitation invitation = getInvitation(invitationId);

		// Only invitee can reject
		if (!invitation.getInviteeId().equals(currentUser.getId())) {

			throw new ForbiddenException("You cannot reject this invitation");
		}

		// Check status
		if (!"PENDING".equalsIgnoreCase(invitation.getStatus())) {

			throw new ConflictException("Invitation is already " + invitation.getStatus().toLowerCase());
		}

		invitation.setStatus("REJECTED");
		invitation.setRespondedAt(LocalDateTime.now());

		EventInvitation updated = invitationRepository.save(invitation);

		return convertToResponseDTO(updated);
	}

	// =========================================================
	// GET RECEIVED INVITATIONS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<EventInvitationResponseDTO> getReceivedInvitations() {

		User currentUser = getCurrentUser();

		return invitationRepository.findByInviteeId(currentUser.getId()).stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	// =========================================================
	// GET SENT INVITATIONS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<EventInvitationResponseDTO> getSentInvitations() {

		User currentUser = getCurrentUser();

		return invitationRepository.findByInviterId(currentUser.getId()).stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	// =========================================================
	// NEW - GET INVITATION BY ID
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public EventInvitationResponseDTO getInvitationById(Long invitationId) {

		User currentUser = getCurrentUser();

		EventInvitation invitation = getInvitation(invitationId);

		// Only inviter or invitee can view it
		if (!invitation.getInviterId().equals(currentUser.getId())
				&& !invitation.getInviteeId().equals(currentUser.getId())) {

			throw new ForbiddenException("You are not allowed to view this invitation");
		}

		return convertToResponseDTO(invitation);
	}

	// =========================================================
	// NEW - CANCEL INVITATION
	// =========================================================

	@Override
	@Transactional
	public EventInvitationResponseDTO cancelInvitation(Long invitationId) {

		User currentUser = getCurrentUser();

		EventInvitation invitation = getInvitation(invitationId);

		// Only inviter can cancel
		if (!invitation.getInviterId().equals(currentUser.getId())) {

			throw new ForbiddenException("Only the inviter can cancel this invitation");
		}

		// Only pending invitation can be cancelled
		if (!"PENDING".equalsIgnoreCase(invitation.getStatus())) {

			throw new ConflictException("Only pending invitations can be cancelled");
		}

		invitation.setStatus("CANCELLED");
		invitation.setRespondedAt(LocalDateTime.now());

		EventInvitation updated = invitationRepository.save(invitation);

		return convertToResponseDTO(updated);
	}

	// =========================================================
	// NEW - GET EVENT INVITATIONS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<EventInvitationResponseDTO> getEventInvitations(Long eventId) {

		User currentUser = getCurrentUser();

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

		// Only event creator can see all invitations
		if (event.getCreatedBy() == null || !event.getCreatedBy().equals(currentUser.getId())) {

			throw new ForbiddenException("Only event creator can view event invitations");
		}

		return invitationRepository.findByEventId(eventId).stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	// =========================================================
	// GET CURRENT USER
	// =========================================================

	private User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null
				|| authentication.getName().isBlank()) {

			throw new ForbiddenException("User is not authenticated");
		}

		String username = authentication.getName();

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));
	}

	// =========================================================
	// GET INVITATION
	// =========================================================

	private EventInvitation getInvitation(Long invitationId) {

		return invitationRepository.findById(invitationId)
				.orElseThrow(() -> new ResourceNotFoundException("Invitation not found with id: " + invitationId));
	}

	// =========================================================
	// CONVERT TO RESPONSE DTO
	// =========================================================

	private EventInvitationResponseDTO convertToResponseDTO(EventInvitation invitation) {

		Event event = eventRepository.findById(invitation.getEventId()).orElse(null);

		User inviter = userRepository.findById(invitation.getInviterId()).orElse(null);

		User invitee = userRepository.findById(invitation.getInviteeId()).orElse(null);

		String eventTitle = event != null ? event.getTitle() : null;

		String inviterUsername = inviter != null ? inviter.getUsername() : null;

		String inviteeUsername = invitee != null ? invitee.getUsername() : null;

		return new EventInvitationResponseDTO(invitation.getId(), invitation.getEventId(), eventTitle,
				invitation.getInviterId(), inviterUsername, invitation.getInviteeId(), inviteeUsername,
				invitation.getStatus(), invitation.getInvitedAt(), invitation.getRespondedAt());
	}
}