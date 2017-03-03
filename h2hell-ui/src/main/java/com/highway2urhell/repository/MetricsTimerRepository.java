package com.highway2urhell.repository;

import com.highway2urhell.domain.MetricsTimer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MetricsTimer entity.
 */
@SuppressWarnings("unused")
public interface MetricsTimerRepository extends JpaRepository<MetricsTimer,Long> {

}
