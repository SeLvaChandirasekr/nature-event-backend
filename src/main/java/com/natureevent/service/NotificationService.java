package com.natureevent.service;

import com.natureevent.entity.Notification;

import java.util.List;

public interface NotificationService {

	Notification createNotification(
			Long userId,
			String type,
			String message,
			Long referenceId);

	List<Notification> getMyNotifications();

	List<Notification> getMyUnreadNotifications();

	long getUnreadNotificationCount();

	Notification markAsRead(Long notificationId);

	void markAllAsRead();
}