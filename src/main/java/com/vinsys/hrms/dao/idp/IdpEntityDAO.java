package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.idp.entity.Idp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdpEntityDAO extends JpaRepository<Idp, Long> {

    boolean existsByEmployeeIdAndRecordStatus(Long employeeId, String recordStatus);
}
