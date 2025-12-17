package com.vinsys.hrms.idp.schedulers;

import com.vinsys.hrms.dao.idp.IdpEmailLogDAO;
import com.vinsys.hrms.dao.idp.IdpFlowHistoryDAO;
import com.vinsys.hrms.idp.entity.IdpEmailLog;
import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.idp.utils.IdpEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.vinsys.hrms.idp.utils.IdpEnums.Status.PENDING;

@Service
public class IdpQueueEmailService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final IdpFlowHistoryDAO idpFlowHistoryDAO;
    private final IdpEmailLogDAO idpEmailLogDAO;

    public IdpQueueEmailService(final IdpFlowHistoryDAO idpFlowHistoryDAO, final IdpEmailLogDAO idpEmailLogDAO) {
        this.idpFlowHistoryDAO = idpFlowHistoryDAO;
        this.idpEmailLogDAO = idpEmailLogDAO;
    }

    public void queueIdpReminderEmail() {
        log.info("Queue Idp email to Employee :::");

        List<IdpFlowHistory> pendingList = idpFlowHistoryDAO.getDataListForFollowupEmail();
        if (CollectionUtils.isEmpty(pendingList)) {
            return;
        }

        for (IdpFlowHistory entity : pendingList) {
            IdpEmailLog saveEmailLog = new IdpEmailLog();
            saveEmailLog.setIdp(entity.getIdp());
            saveEmailLog.setIdpFlow(entity);
            saveEmailLog.setActionType(IdpEnums.EmailType.FOLLOWUP.getKey());
            saveEmailLog.setStatus(PENDING.getKey());
            idpEmailLogDAO.save(saveEmailLog);
        }
    }
}
