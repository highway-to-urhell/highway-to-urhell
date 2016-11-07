package com.highway2urhell.repository;

import com.highway2urhell.domain.Analysis;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Analysis entity.
 */
@SuppressWarnings("unused")
public interface AnalysisRepository extends JpaRepository<Analysis,Long> {

}
