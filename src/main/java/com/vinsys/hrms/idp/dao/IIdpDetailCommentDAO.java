package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpDetailComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIdpDetailCommentDAO extends JpaRepository<IdpDetailComment, Long> {

    IdpDetailComment findByIdpIdAndIdpDetailIdAndIdpFlowId(Long idpId, Long idpDetailId, Long idpFlowId);
}