package com.natureevent.service;

import com.natureevent.entity.EventImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventImageService {

	EventImage uploadImage(Long eventId, MultipartFile file);

	List<EventImage> getEventImages(Long eventId);

	void deleteImage(Long eventId, Long imageId);
}