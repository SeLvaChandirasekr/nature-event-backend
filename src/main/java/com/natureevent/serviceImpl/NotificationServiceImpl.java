package com.natureevent.serviceImpl;

import com.natureevent.entity.Notification;
import com.natureevent.entity.User;
import com.natureevent.exception.ForbiddenException;
import com.natureevent.exception.ResourceNotFoundException;
import com.natureevent.repository.NotificationRepository;
import com.natureevent.repository.UserRepository;
import com.natureevent.service.NotificationService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {

		this.notificationRepository = notificationRepository;
		this.userRepository = userRepository;
	}

	// =========================================================
	// CREATE NOTIFICATION
	// =========================================================

	@Override
	@Transactional
	public Notification createNotification(Long userId, String type, String message, Long referenceId) {

		// Check whether user exists
		userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		Notification notification = new Notification();

		notification.setUserId(userId);
		notification.setType(type);
		notification.setMessage(message);
		notification.setReferenceId(referenceId);
		notification.setRead(false);
		notification.setCreatedAt(LocalDateTime.now());

		return notificationRepository.save(notification);
	}

	// =========================================================
	// GET MY NOTIFICATIONS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<Notification> getMyNotifications() {

		User currentUser = getCurrentUser();

		return notificationRepository.findByUserId(currentUser.getId());
	}

	// =========================================================
	// GET MY UNREAD NOTIFICATIONS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<Notification> getMyUnreadNotifications() {

		User currentUser = getCurrentUser();

		return notificationRepository.findUnreadByUserId(currentUser.getId());
	}

	// =========================================================
	// GET UNREAD NOTIFICATION COUNT
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public long getUnreadNotificationCount() {

		User currentUser = getCurrentUser();

		return notificationRepository.countUnreadByUserId(currentUser.getId());
	}

	// =========================================================
	// MARK ONE NOTIFICATION AS READ
	// =========================================================

	@Override
	@Transactional
	public Notification markAsRead(Long notificationId) {

		User currentUser = getCurrentUser();

		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

		// User can only update their own notification
		if (!notification.getUserId().equals(currentUser.getId())) {

			throw new ForbiddenException("You are not allowed to update this notification");
		}

		notification.setRead(true);

		return notificationRepository.save(notification);
	}

	// =========================================================
	// MARK ALL NOTIFICATIONS AS READ
	// =========================================================

	@Override
	@Transactional
	public void markAllAsRead() {

		User currentUser = getCurrentUser();

		List<Notification> notifications = notificationRepository.findByUserId(currentUser.getId());

		for (Notification notification : notifications) {

			if (!notification.isRead()) {

				notification.setRead(true);

				notificationRepository.save(notification);
			}
		}
	}

	// =========================================================
	// GET CURRENT LOGGED-IN USER
	// =========================================================

	private User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {

			throw new ForbiddenException("User is not authenticated");
		}

		String username = authentication.getName();

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));
	}
}