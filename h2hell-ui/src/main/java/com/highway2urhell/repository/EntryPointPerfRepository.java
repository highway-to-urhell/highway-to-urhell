package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPointPerf;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPointPerf entity.
 */
@SuppressWarnings("unused")
public interface EntryPointPerfRepository extends JpaRepository<EntryPointPerf,Long> {

}
