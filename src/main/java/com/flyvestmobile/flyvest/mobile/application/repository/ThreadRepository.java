package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
}
