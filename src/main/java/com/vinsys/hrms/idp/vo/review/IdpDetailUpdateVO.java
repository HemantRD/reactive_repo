package com.vinsys.hrms.idp.vo.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents each element inside the idpDetails list.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpDetailUpdateVO {

    /** Detail identifier. */
    private Long id;

    /** Optional comment. */
    private String comment;
}