package com.example.final_project_java.event.repository;

import com.example.final_project_java.event.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {

    Optional<Event> findByEventNo(int eventNo);

}