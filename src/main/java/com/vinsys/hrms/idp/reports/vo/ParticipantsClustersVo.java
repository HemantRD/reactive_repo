package com.vinsys.hrms.idp.reports.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParticipantsClustersVo {
    public Long id;
    public String trainingCode;
    public String trainingName;
    public Boolean isInternal;
    public Integer priority;
    public String trainingType;
    public String trainingGroupCode;
    public Double trainingCost;
    public Long memberCount;
    public Double totalCost;
    public List<ParticipantsClusterDetailsVo> participantDetails;

    public ParticipantsClustersVo(Long id, String trainingCode, String trainingName, Boolean isInternal, Integer priority, String trainingType, String trainingGroupCode, Double trainingCost, Long memberCount, Double totalCost) {
        this.id = id;
        this.trainingCode = trainingCode;
        this.trainingName = trainingName;
        this.isInternal = isInternal;
        this.priority = priority;
        this.trainingType = trainingType;
        this.trainingGroupCode = trainingGroupCode;
        this.trainingCost = trainingCost;
        this.memberCount = memberCount;
        this.totalCost = totalCost;
        this.participantDetails = new ArrayList<>();
    }
}

