package com.natureevent.repository;

import com.natureevent.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

	Notification save(Notification notification);

	Optional<Notification> findById(Long id);

	List<Notification> findByUserId(Long userId);

	List<Notification> findUnreadByUserId(Long userId);

	long countUnreadByUserId(Long userId);
}