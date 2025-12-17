package com.vinsys.hrms.idp.reports.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupVsIndividualTrainingCountSummary {
    private int totalTrainingCount;
    private int groupTrainingCount;
    private int individualTrainingCount;
}

