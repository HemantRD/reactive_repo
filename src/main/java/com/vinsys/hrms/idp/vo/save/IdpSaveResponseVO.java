package com.vinsys.hrms.idp.vo.save;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdpSaveResponseVO {
    private String status;
    private DataVO data;

    @Data
    @Builder
    public static class DataVO {
        private Long id;
        private Long activeFlowId;
    }
}
