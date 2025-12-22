package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.MapEmployeeIdpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMapEmployeeIdpStatusDAO  extends JpaRepository<MapEmployeeIdpStatus, Long> {
    Optional<MapEmployeeIdpStatus> findByEmployeeId(Long employeeId);
}
