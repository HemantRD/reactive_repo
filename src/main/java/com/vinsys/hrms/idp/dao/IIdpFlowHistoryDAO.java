package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIdpFlowHistoryDAO extends JpaRepository<IdpFlowHistory, Long> {
}
