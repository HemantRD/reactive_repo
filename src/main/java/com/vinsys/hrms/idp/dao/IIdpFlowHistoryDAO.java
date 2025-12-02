package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.idp.entity.IdpProgressHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IIdpFlowHistoryDAO extends JpaRepository<IdpFlowHistory, Long> {
    List<IdpFlowHistory> findByIdpId(Long idpId);
}
