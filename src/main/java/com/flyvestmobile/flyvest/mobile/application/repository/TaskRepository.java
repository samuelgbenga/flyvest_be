package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
