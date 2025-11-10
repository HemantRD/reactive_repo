package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.idp.entity.IdpEmailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdpEmailLogDAO extends JpaRepository<IdpEmailLogEntity, Long> {

    List<IdpEmailLogEntity> findByStatus(String status);
}
