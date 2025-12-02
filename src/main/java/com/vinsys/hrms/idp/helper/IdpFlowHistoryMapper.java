package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.idp.enumconstant.IdpFlowHistoryActionType;
import com.vinsys.hrms.idp.enumconstant.IdpFlowHistoryStatus;

import java.util.EnumMap;
import java.util.Map;

public class IdpFlowHistoryMapper {

    private static final Map<IdpFlowHistoryActionType, IdpFlowHistoryStatus> ACTION_TO_STATUS_MAP;

    static {
        ACTION_TO_STATUS_MAP = new EnumMap<>(IdpFlowHistoryActionType.class);

        ACTION_TO_STATUS_MAP.put(IdpFlowHistoryActionType.SUBMIT,  IdpFlowHistoryStatus.SUBMITTED);
        ACTION_TO_STATUS_MAP.put(IdpFlowHistoryActionType.APPROVE, IdpFlowHistoryStatus.APPROVED);
        ACTION_TO_STATUS_MAP.put(IdpFlowHistoryActionType.REJECT,  IdpFlowHistoryStatus.REJECTED);
        ACTION_TO_STATUS_MAP.put(IdpFlowHistoryActionType.COMPLETE, IdpFlowHistoryStatus.COMPLETED);
        ACTION_TO_STATUS_MAP.put(IdpFlowHistoryActionType.ABORT,   IdpFlowHistoryStatus.ABORTED);

        // Pending action → Pending status (if required)
        ACTION_TO_STATUS_MAP.put(IdpFlowHistoryActionType.PENDING, IdpFlowHistoryStatus.PENDING);
    }

    public static IdpFlowHistoryStatus getStatus(IdpFlowHistoryActionType actionType) {
        return ACTION_TO_STATUS_MAP.getOrDefault(actionType, IdpFlowHistoryStatus.DRAFT);
    }
}
