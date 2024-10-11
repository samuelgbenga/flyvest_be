package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
