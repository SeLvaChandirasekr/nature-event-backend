package com.natureevent.dto;

import java.time.LocalDateTime;

public class EventInvitationResponseDTO {

	private Long id;

	private Long eventId;

	private String eventTitle;

	private Long inviterId;

	private String inviterUsername;

	private Long inviteeId;

	private String inviteeUsername;

	private String status;

	private LocalDateTime invitedAt;

	private LocalDateTime respondedAt;

	public EventInvitationResponseDTO() {
	}

	public EventInvitationResponseDTO(Long id, Long eventId, String eventTitle, Long inviterId, String inviterUsername,
			Long inviteeId, String inviteeUsername, String status, LocalDateTime invitedAt, LocalDateTime respondedAt) {

		this.id = id;
		this.eventId = eventId;
		this.eventTitle = eventTitle;
		this.inviterId = inviterId;
		this.inviterUsername = inviterUsername;
		this.inviteeId = inviteeId;
		this.inviteeUsername = inviteeUsername;
		this.status = status;
		this.invitedAt = invitedAt;
		this.respondedAt = respondedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Long getInviterId() {
		return inviterId;
	}

	public void setInviterId(Long inviterId) {
		this.inviterId = inviterId;
	}

	public String getInviterUsername() {
		return inviterUsername;
	}

	public void setInviterUsername(String inviterUsername) {
		this.inviterUsername = inviterUsername;
	}

	public Long getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(Long inviteeId) {
		this.inviteeId = inviteeId;
	}

	public String getInviteeUsername() {
		return inviteeUsername;
	}

	public void setInviteeUsername(String inviteeUsername) {
		this.inviteeUsername = inviteeUsername;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getInvitedAt() {
		return invitedAt;
	}

	public void setInvitedAt(LocalDateTime invitedAt) {
		this.invitedAt = invitedAt;
	}

	public LocalDateTime getRespondedAt() {
		return respondedAt;
	}

	public void setRespondedAt(LocalDateTime respondedAt) {
		this.respondedAt = respondedAt;
	}
}
