package com.vinsys.hrms.idp.reports.vo;

import lombok.Data;

@Data
public class ParticipantsClustersListReq {
    private String keyword;
    private Boolean isInternal;
    public Integer priority;
    public String trainingType;
    private String sortBy;
    private String sortType;
}

