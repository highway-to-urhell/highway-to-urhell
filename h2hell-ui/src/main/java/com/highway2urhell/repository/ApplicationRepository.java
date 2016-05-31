package com.highway2urhell.repository;

import com.highway2urhell.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    @Query("select application from Application application left join fetch application.analyses where application.token =:token")
    List<Application> findByTokenWithEagerRelationships(@Param("token") String token);

}
