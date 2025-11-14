package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.idp.entity.IdpProgressHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IdpProgressHistoryDAO extends JpaRepository<IdpProgressHistoryEntity, Long> {

    @Modifying
    @Query("update IdpProgressHistoryEntity set isActive='N' where idp.id=:idpId" +
            "  and idpDetail.id=:idpDetailId and employeeId=:employeeId and isActive='Y'")
    void markOldRecordsInActive(Long idpId, Long idpDetailId, Long employeeId);


}
