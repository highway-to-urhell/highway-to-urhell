package com.highway2urhell.repository;

import com.highway2urhell.domain.EntryPointParameters;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EntryPointParameters entity.
 */
@SuppressWarnings("unused")
public interface EntryPointParametersRepository extends JpaRepository<EntryPointParameters,Long> {

}
