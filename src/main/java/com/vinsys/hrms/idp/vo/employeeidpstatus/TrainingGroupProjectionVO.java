package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingGroupProjectionVO {
    private Long trainingId;
    private Long participantCount;
    private Long maxIdpDetailId;
}
