package com.highway2urhell.repository;

import com.highway2urhell.domain.Application;

import com.highway2urhell.service.dto.ApplicationByType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    @Query(value = "select new com.highway2urhell.service.dto.ApplicationByType(a.appType, count(a)) from com.highway2urhell.domain.Application a group by a.appType")
    List<ApplicationByType> countGroupByAppType();

    @Query(value = "select application from com.highway2urhell.domain.Application application where application.token =:token")
    Application findByToken(@Param("token") String token);
}
