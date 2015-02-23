package io.highway.to.urhell.dao;

import io.highway.to.urhell.domain.BreakerLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BreakerLogDao extends JpaRepository<BreakerLog, String> {

	@Transactional(readOnly = true)
	List<BreakerLog> findByToken(String token);

	@Transactional(readOnly = true)
	@Query("select count(bl) from BreakerLog bl where bl.pathClassMethodName=(:pathClassMethodName) and bl.token=(:token)")
	Long findByPathClassMethodNameAndToken(
			@Param("pathClassMethodName") String pathClassMethodName,
			@Param("token") String token);
}
