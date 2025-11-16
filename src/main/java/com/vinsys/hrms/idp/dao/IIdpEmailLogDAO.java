package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIdpEmailLogDAO extends JpaRepository<IdpEmailLog, Long> {

}