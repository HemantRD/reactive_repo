package com.vinsys.hrms.idp.t_types.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TTypesVo {
    private Long id;
    private String name;
    private String remark;
    private Boolean status;

    public TTypesVo(Long id, String name, String remark, String status) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.status = status.equalsIgnoreCase("Y");
    }
}
