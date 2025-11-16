package com.vinsys.hrms.dao.idp;


import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IdpFlowHistoryDAO extends JpaRepository<IdpFlowHistory, Long> {

    @Query(" SELECT i FROM IdpFlowHistory i " +
            "where recordStatus='Y' and actionType='Pending' " +
            "and current_date=actionDate+1 ")
    List<IdpFlowHistory> getDataListForFollowupEmail();
}
