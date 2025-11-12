package com.vinsys.hrms.dao.idp;


import com.vinsys.hrms.idp.entity.IdpFlowHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IdpFlowHistoryDAO extends JpaRepository<IdpFlowHistoryEntity, Long> {

    @Query(" SELECT i FROM IdpFlowHistoryEntity i " +
            "where recordStatus='Y' and actionType='Pending' " +
            "and current_date=actionDate+1 ")
    List<IdpFlowHistoryEntity> getDataListForFollowupEmail();
}
