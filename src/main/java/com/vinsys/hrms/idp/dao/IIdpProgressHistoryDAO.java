package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpProgressHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IIdpProgressHistoryDAO extends JpaRepository<IdpProgressHistory, Long> {
    List<IdpProgressHistory> findByIdpId(Long idpId);
}
