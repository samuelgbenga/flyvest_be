package com.flyvestmobile.flyvest.mobile.application.repository;


import com.flyvestmobile.flyvest.mobile.application.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
