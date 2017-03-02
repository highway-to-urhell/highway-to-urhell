package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPointPerf;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPointPerf entity.
 */
@SuppressWarnings("unused")
public interface EntryPointPerfRepository extends JpaRepository<EntryPointPerf,Long> {

    @Query("select AVG(entryPointPerf.timeExec) from EntryPointPerf entryPointPerf, EntryPoint entryPoint, Analysis analysis, Application app where entryPoint.pathClassMethodName=:pathClassMethodName and app.token =:token")
    Long findAverageFromPathClassMethodNameAndToken(@Param("pathClassMethodName") String pathClassMethodName, @Param("token") String token);
}
