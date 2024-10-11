package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
