package com.natureevent.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_invitations", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "event_id", "inviter_id", "invitee_id" }) })
public class EventInvitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false)
	private Long eventId;

	@Column(name = "inviter_id", nullable = false)
	private Long inviterId;

	@Column(name = "invitee_id", nullable = false)
	private Long inviteeId;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private LocalDateTime invitedAt;

	private LocalDateTime respondedAt;

	public EventInvitation() {
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

	public Long getInviterId() {
		return inviterId;
	}

	public void setInviterId(Long inviterId) {
		this.inviterId = inviterId;
	}

	public Long getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(Long inviteeId) {
		this.inviteeId = inviteeId;
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
