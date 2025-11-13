package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.idp.entity.IdpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdpEntityDAO extends JpaRepository<IdpEntity, Long> {

    boolean existsByEmployeeIdAndRecordStatus(Long employeeId, String recordStatus);
}
