package com.vinsys.hrms.idp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vinsys.hrms.idp.entity.IdpDetailComment;
import java.util.List;
import java.util.Optional;

public interface IIdpDetailCommentDAO extends JpaRepository<IdpDetailComment, Long> {

}