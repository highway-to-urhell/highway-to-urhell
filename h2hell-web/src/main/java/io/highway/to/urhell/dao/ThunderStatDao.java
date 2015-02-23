package io.highway.to.urhell.dao;

import io.highway.to.urhell.domain.ThunderStat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ThunderStatDao extends JpaRepository<ThunderStat, String> {

	@Transactional(readOnly = true)
	@Query("from ThunderStat th where th.pathClassMethodName=(:pathClassMethodName) and th.thunderApp.token=(:token)")
	ThunderStat findByPathClassMethodNameAndToken(
			@Param("pathClassMethodName") String pathClassMethodName,
			@Param("token") String token);
	
	
	@Transactional(readOnly = true)
	@Query("from ThunderStat th where th.thunderApp.token=(:token) order by th.count desc ")
	List<ThunderStat> findByToken(@Param("token") String token);

}
