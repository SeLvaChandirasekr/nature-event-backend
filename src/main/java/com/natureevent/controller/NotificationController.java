package com.natureevent.controller;

import com.natureevent.entity.Notification;
import com.natureevent.service.NotificationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	// =========================================================
	// GET ALL MY NOTIFICATIONS
	// =========================================================

	@GetMapping
	public List<Notification> getMyNotifications() {

		return notificationService.getMyNotifications();
	}

	// =========================================================
	// GET MY UNREAD NOTIFICATIONS
	// =========================================================

	@GetMapping("/unread")
	public List<Notification> getMyUnreadNotifications() {

		return notificationService.getMyUnreadNotifications();
	}

	// =========================================================
	// GET UNREAD NOTIFICATION COUNT
	// =========================================================

	@GetMapping("/unread/count")
	public long getUnreadNotificationCount() {

		return notificationService.getUnreadNotificationCount();
	}

	// =========================================================
	// MARK ONE NOTIFICATION AS READ
	// =========================================================

	@PutMapping("/{notificationId}/read")
	public Notification markAsRead(@PathVariable Long notificationId) {

		return notificationService.markAsRead(notificationId);
	}

	// =========================================================
	// MARK ALL NOTIFICATIONS AS READ
	// =========================================================

	@PutMapping("/read-all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void markAllAsRead() {

		notificationService.markAllAsRead();
	}
}