package com.vinsys.hrms.idp.reports.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsClustersVo {
    public int id;
    public String trainingCode;
    public String trainingName;
    public Boolean isInternal;
    public Integer priority;
    public String trainingType;
    public Double trainingCost;
    public int memberCount;
    public Double totalCost;
}

