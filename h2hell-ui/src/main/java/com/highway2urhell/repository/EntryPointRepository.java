package com.highway2urhell.repository;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.EntryPoint;

import com.highway2urhell.service.dto.EntryPointByApplication;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPoint entity.
 */
@SuppressWarnings("unused")
public interface EntryPointRepository extends JpaRepository<EntryPoint,Long> {

    @Query("select entryPoint from EntryPoint entryPoint, Analysis analysis, Application app where analysis.id =:analysisId and entryPoint.pathClassMethodName =:pathClassMethodName")
    EntryPoint findByPathClassMethodNameAndToken(@Param("pathClassMethodName") String pathClassMethodName, @Param("analysisId") long analysisId);

    @Query(value = "select new com.highway2urhell.service.dto.EntryPointByApplication(app.name, analysis.appVersion, count(entryPoint)) from EntryPoint entryPoint, Analysis analysis, Application app group by app.name,analysis.appVersion")
    List<EntryPointByApplication> countGroupByApplication();

    @Query("select entryPoint from EntryPoint entryPoint, Analysis analysis, Application app where analysis.application.token =:token")
    List<EntryPoint> findAllByTokenWithApplicationAndEntrypoint(@Param("token") String token);

}
