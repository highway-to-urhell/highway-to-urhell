package com.highway2urhell.repository;

import com.highway2urhell.domain.UserPermission;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserPermission entity.
 */
@SuppressWarnings("unused")
public interface UserPermissionRepository extends JpaRepository<UserPermission,Long> {

    @Query("select distinct userPermission from UserPermission userPermission left join fetch userPermission.projects")
    List<UserPermission> findAllWithEagerRelationships();

    @Query("select userPermission from UserPermission userPermission left join fetch userPermission.projects where userPermission.id =:id")
    UserPermission findOneWithEagerRelationships(@Param("id") Long id);

}
