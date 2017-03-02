package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPointCall;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPointCall entity.
 */
@SuppressWarnings("unused")
public interface EntryPointCallRepository extends JpaRepository<EntryPointCall,Long> {

    @Query("select COUNT(entryPointCall) from EntryPointCall entryPointCall, EntryPoint entryPoint, Analysis analysis, Application app where entryPoint.pathClassMethodName=:pathClassMethodName and app.token =:token")
    Long findByPathClassMethodNameAndToken(@Param("pathClassMethodName") String pathClassMethodName, @Param("token") String token);
}
