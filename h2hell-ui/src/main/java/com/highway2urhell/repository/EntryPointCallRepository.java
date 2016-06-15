package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPointCall;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPointCall entity.
 */
@SuppressWarnings("unused")
public interface EntryPointCallRepository extends JpaRepository<EntryPointCall,Long> {

}
