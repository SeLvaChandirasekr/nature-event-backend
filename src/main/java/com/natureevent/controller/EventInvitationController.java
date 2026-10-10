package com.natureevent.controller;

import com.natureevent.dto.EventInvitationRequestDTO;
import com.natureevent.dto.EventInvitationResponseDTO;
import com.natureevent.service.EventInvitationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class EventInvitationController {

	private final EventInvitationService invitationService;

	public EventInvitationController(EventInvitationService invitationService) {

		this.invitationService = invitationService;
	}

	// =========================================================
	// INVITE USER
	// =========================================================

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventInvitationResponseDTO inviteUser(@Valid @RequestBody EventInvitationRequestDTO request) {

		return invitationService.inviteUser(request);
	}

	// =========================================================
	// ACCEPT INVITATION
	// =========================================================

	@PutMapping("/{invitationId}/accept")
	public EventInvitationResponseDTO acceptInvitation(@PathVariable Long invitationId) {

		return invitationService.acceptInvitation(invitationId);
	}

	// =========================================================
	// REJECT INVITATION
	// =========================================================

	@PutMapping("/{invitationId}/reject")
	public EventInvitationResponseDTO rejectInvitation(@PathVariable Long invitationId) {

		return invitationService.rejectInvitation(invitationId);
	}

	// =========================================================
	// RECEIVED INVITATIONS
	// =========================================================

	@GetMapping("/received")
	public List<EventInvitationResponseDTO> getReceivedInvitations() {

		return invitationService.getReceivedInvitations();
	}

	// =========================================================
	// SENT INVITATIONS
	// =========================================================

	@GetMapping("/sent")
	public List<EventInvitationResponseDTO> getSentInvitations() {

		return invitationService.getSentInvitations();
	}

	// =========================================================
	// GET INVITATION BY ID
	// =========================================================

	@GetMapping("/{invitationId}")
	public EventInvitationResponseDTO getInvitationById(@PathVariable Long invitationId) {

		return invitationService.getInvitationById(invitationId);
	}

	// =========================================================
	// CANCEL INVITATION
	// =========================================================

	@PutMapping("/{invitationId}/cancel")
	public EventInvitationResponseDTO cancelInvitation(@PathVariable Long invitationId) {

		return invitationService.cancelInvitation(invitationId);
	}

	// =========================================================
	// GET EVENT INVITATIONS
	// =========================================================

	@GetMapping("/event/{eventId}")
	public List<EventInvitationResponseDTO> getEventInvitations(@PathVariable Long eventId) {

		return invitationService.getEventInvitations(eventId);
	}
}