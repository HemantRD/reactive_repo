package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.idp.entity.IdpEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdpEmailLogDAO extends JpaRepository<IdpEmailLog, Long> {

    List<IdpEmailLog> findByStatus(String status);
}
