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

    @Query("select distinct analysis from Analysis analysis left join fetch analysis.application app where app.token =:token")
    List<Analysis> findAllByTokenWithApplication(@Param("token") String token);

    @Query("select distinct analysis from Analysis analysis left join fetch analysis.application app where app.token =:token and analysis.appVersion =:version")
    Analysis findOneByTokenAndVersionWithApplication(@Param("token") String token, @Param("version") String version);

    @Query("select distinct analysis from Analysis analysis left join fetch analysis.application")
    List<Analysis> findAllWithApplication();
}
