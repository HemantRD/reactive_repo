package com.vinsys.hrms.idp.vo.getidp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Wrapper response VO for getAllIdp API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpListResponseVO {

    private String status;

    private DataWrapper data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataWrapper {
        private List<IdpListItemVO> idpList;
    }
}
