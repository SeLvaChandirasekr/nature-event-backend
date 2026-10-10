package com.natureevent.controller;

import com.natureevent.entity.EventImage;
import com.natureevent.service.EventImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventImageController {

	private final EventImageService eventImageService;

	public EventImageController(EventImageService eventImageService) {
		this.eventImageService = eventImageService;
	}

	@PostMapping(value = "/{eventId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public EventImage uploadImage(@PathVariable Long eventId, @RequestParam("file") MultipartFile file) {

		return eventImageService.uploadImage(eventId, file);
	}

	@GetMapping("/{eventId}/images")
	public List<EventImage> getEventImages(@PathVariable Long eventId) {

		return eventImageService.getEventImages(eventId);
	}

	@DeleteMapping("/{eventId}/images/{imageId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteImage(@PathVariable Long eventId, @PathVariable Long imageId) {

		eventImageService.deleteImage(eventId, imageId);
	}
}