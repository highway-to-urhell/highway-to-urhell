package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the EntryPoint entity.
 */
@SuppressWarnings("unused")
public interface EntryPointRepository extends JpaRepository<EntryPoint,Long> {

@Query("select entryPoint from EntryPoint entryPoint, Analysis analysis, Application app where app.token =:token and entryPoint.pathClassMethodName =:pathClassMethodName")
    EntryPoint findByPathClassMethodNameAndToken(@Param("pathClassMethodName") String pathClassMethodName, @Param("token") String token);
}
