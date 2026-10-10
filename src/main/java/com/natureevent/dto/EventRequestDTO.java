package com.natureevent.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventRequestDTO {

	@NotBlank(message = "Title is required")
	@Size(max = 150, message = "Title cannot exceed 150 characters")
	private String title;

	@Size(max = 2000, message = "Description cannot exceed 2000 characters")
	private String description;

	@NotBlank(message = "Event type is required")
	@Size(max = 50, message = "Event type cannot exceed 50 characters")
	private String eventType;

	@NotBlank(message = "Location is required")
	private String location;

	@NotNull(message = "Event date is required")
	@FutureOrPresent(message = "Event date cannot be in the past")
	private LocalDate eventDate;

	@NotNull(message = "Event time is required")
	private LocalTime eventTime;

	@NotNull(message = "Maximum participants is required")
	@Min(value = 1, message = "Maximum participants must be at least 1")
	private Integer maxParticipants;

	public EventRequestDTO() {
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
}