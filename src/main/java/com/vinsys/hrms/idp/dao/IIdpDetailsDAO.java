package com.vinsys.hrms.idp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vinsys.hrms.idp.entity.IdpDetails;
import java.util.List;
import java.util.Optional;

@Repository
public interface IIdpDetailsDAO extends JpaRepository<IdpDetails, Long> {

}