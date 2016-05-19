package com.highway2urhell.repository;

import com.highway2urhell.domain.Application;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {

}
