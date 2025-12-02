package com.vinsys.hrms.idp.reports.vo;

import lombok.Data;

import java.util.List;

@Data
public class ParticipantsClustersVo {
    public int id;
    public String trainingCode;
    public String trainingName;
    public String isInternal;
    public String priority;
    public String trainingType;
    public int memberCount;
    public String trainingCost;
}

