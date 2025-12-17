package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.vo.getidp.IdpDetailsComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.vinsys.hrms.idp.entity.IdpDetails;

import java.util.List;

@Repository
public interface IIdpDetailsDAO extends JpaRepository<IdpDetails, Long> {

    List<IdpDetails> findAllByIdpId(Long idpId);

    @Query("SELECT new com.vinsys.hrms.idp.vo.getidp.IdpDetailsComments( " +
            "h.employeeRole, c.comment, h.actionType, h.actionDate) " +
            "FROM IdpDetailComment c " +
            "JOIN IdpFlowHistory h ON c.idpFlowId = h.id " +
            "WHERE c.idpDetailId = :detailId " +
            "ORDER BY h.actionDate ASC")
    List<IdpDetailsComments> findCommentsWithRole(Long detailId);

}