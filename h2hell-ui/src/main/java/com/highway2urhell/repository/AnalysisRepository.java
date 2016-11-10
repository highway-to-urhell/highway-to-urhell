package com.highway2urhell.repository;

import com.highway2urhell.domain.Analysis;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Analysis entity.
 */
@SuppressWarnings("unused")
public interface AnalysisRepository extends JpaRepository<Analysis,Long> {

    @Query("select distinct analysis from Analysis analysis left join fetch analysis.application where analysis.application.token =:token")
    List<Analysis> findByTokenWithEagerRelationships(@Param("token") String token);

}
