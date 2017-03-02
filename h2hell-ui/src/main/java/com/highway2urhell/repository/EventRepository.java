package com.highway2urhell.repository;

import com.highway2urhell.domain.Event;

import com.highway2urhell.web.rest.dto.v1api.MessageEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query(value = "select new com.highway2urhell.web.rest.dto.v1api.MessageEvent(event) from Event event, Analysis analysis, Application app where analysis.application.token =:token")
    List<MessageEvent> findByTokenAndReference(@Param("token") String token);
}
