package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPoint entity.
 */
@SuppressWarnings("unused")
public interface EntryPointRepository extends JpaRepository<EntryPoint,Long> {

}
