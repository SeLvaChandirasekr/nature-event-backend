package com.natureevent.serviceImpl;

import com.natureevent.entity.EventImage;
import com.natureevent.exception.ResourceNotFoundException;
import com.natureevent.repository.EventImageRepository;
import com.natureevent.repository.EventRepository;
import com.natureevent.service.EventImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventImageServiceImpl implements EventImageService {

	private final EventImageRepository eventImageRepository;
	private final EventRepository eventRepository;

	@Value("${app.upload-dir:uploads/event-images}")
	private String uploadDir;

	public EventImageServiceImpl(EventImageRepository eventImageRepository, EventRepository eventRepository) {
		this.eventImageRepository = eventImageRepository;
		this.eventRepository = eventRepository;
	}

	@Override
	@Transactional
	public EventImage uploadImage(Long eventId, MultipartFile file) {

		// Check whether event exists
		if (!eventRepository.existsById(eventId)) {
			throw new ResourceNotFoundException("Event not found with id: " + eventId);
		}

		// Validate file
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("Image file is required");
		}

		String contentType = file.getContentType();

		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("Only image files are allowed");
		}

		try {
			// Create upload directory if it doesn't exist
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Generate unique file name
			String originalFileName = file.getOriginalFilename();

			String extension = "";

			if (originalFileName != null && originalFileName.contains(".")) {
				extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			String uniqueFileName = UUID.randomUUID() + extension;

			Path filePath = uploadPath.resolve(uniqueFileName);

			// Save physical file
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			// Save metadata in database
			EventImage eventImage = new EventImage();

			eventImage.setEventId(eventId);
			eventImage.setFileName(originalFileName);
			eventImage.setFilePath(filePath.toString());
			eventImage.setContentType(contentType);
			eventImage.setFileSize(file.getSize());
			eventImage.setUploadedAt(LocalDateTime.now());

			return eventImageRepository.save(eventImage);

		} catch (IOException e) {
			throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventImage> getEventImages(Long eventId) {

		if (!eventRepository.existsById(eventId)) {
			throw new ResourceNotFoundException("Event not found with id: " + eventId);
		}

		return eventImageRepository.findByEventId(eventId);
	}

	@Override
	@Transactional
	public void deleteImage(Long eventId, Long imageId) {

		EventImage eventImage = eventImageRepository.findById(imageId)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

		// Make sure image belongs to the requested event
		if (!eventImage.getEventId().equals(eventId)) {
			throw new IllegalArgumentException("Image does not belong to event with id: " + eventId);
		}

		try {
			// Delete physical file
			if (eventImage.getFilePath() != null) {

				Path filePath = Paths.get(eventImage.getFilePath());

				Files.deleteIfExists(filePath);
			}

			// Delete database record
			eventImageRepository.deleteById(imageId);

		} catch (IOException e) {
			throw new RuntimeException("Failed to delete image: " + e.getMessage(), e);
		}
	}
}
