package com.natureevent.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventResponseDTO {

	private Long id;

	private String title;

	private String description;

	private String eventType;

	private String location;

	private LocalDate eventDate;

	private LocalTime eventTime;

	private Integer maxParticipants;

	private Long createdBy;

	private Integer participantCount;

	private String status;

	private String shareLink;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	// =========================================================
	// CONSTRUCTOR
	// =========================================================

	public EventResponseDTO() {
	}

	public EventResponseDTO(Long id, String title, String description, String eventType, String location,
			LocalDate eventDate, LocalTime eventTime, Integer maxParticipants, Long createdBy, Integer participantCount,
			String status, String shareLink, LocalDateTime createdAt, LocalDateTime updatedAt) {

		this.id = id;
		this.title = title;
		this.description = description;
		this.eventType = eventType;
		this.location = location;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.maxParticipants = maxParticipants;
		this.createdBy = createdBy;
		this.participantCount = participantCount;
		this.status = status;
		this.shareLink = shareLink;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// =========================================================
	// GETTERS AND SETTERS
	// =========================================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public LocalTime getEventTime() {
		return eventTime;
	}

	public void setEventTime(LocalTime eventTime) {
		this.eventTime = eventTime;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getParticipantCount() {
		return participantCount;
	}

	public void setParticipantCount(Integer participantCount) {
		this.participantCount = participantCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
