package com.vinsys.hrms.idp.vo.getidp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents each IDP detail object inside "idpDetails".
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpDetailRequestVO {

    /** Detail record identifier. */
    private Long id;

    /** Optional remark or comment. */
    private String comment;
}