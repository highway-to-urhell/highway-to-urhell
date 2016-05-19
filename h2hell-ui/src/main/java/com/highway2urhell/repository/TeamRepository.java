package com.highway2urhell.repository;

import com.highway2urhell.domain.Team;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
@SuppressWarnings("unused")
public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query("select distinct team from Team team left join fetch team.members")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.members where team.id =:id")
    Team findOneWithEagerRelationships(@Param("id") Long id);

}
