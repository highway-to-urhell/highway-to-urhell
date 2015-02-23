package io.highway.to.urhell.dao;

import io.highway.to.urhell.domain.ThunderApp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ThunderAppDao extends JpaRepository<ThunderApp, String> {

	@Transactional(readOnly = true)
	ThunderApp findByToken(String token);

}
