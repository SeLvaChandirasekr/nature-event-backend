package com.natureevent.service;

import com.natureevent.dto.EventInvitationRequestDTO;
import com.natureevent.dto.EventInvitationResponseDTO;

import java.util.List;

public interface EventInvitationService {

	EventInvitationResponseDTO inviteUser(EventInvitationRequestDTO request);

	EventInvitationResponseDTO acceptInvitation(Long invitationId);

	EventInvitationResponseDTO rejectInvitation(Long invitationId);

	List<EventInvitationResponseDTO> getReceivedInvitations();

	List<EventInvitationResponseDTO> getSentInvitations();

	EventInvitationResponseDTO getInvitationById(Long invitationId);

	EventInvitationResponseDTO cancelInvitation(Long invitationId);

	List<EventInvitationResponseDTO> getEventInvitations(Long eventId);
}