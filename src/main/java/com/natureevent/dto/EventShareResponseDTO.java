package com.natureevent.dto;

public class EventShareResponseDTO {

	private Long eventId;
	private String eventTitle;
	private String shareUrl;

	public EventShareResponseDTO() {
	}

	public EventShareResponseDTO(Long eventId, String eventTitle, String shareUrl) {
		this.eventId = eventId;
		this.eventTitle = eventTitle;
		this.shareUrl = shareUrl;
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

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
}
