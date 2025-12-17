package com.vinsys.hrms.idp.trainingcatalog.vo;

import lombok.Data;

@Data
public class TrainingCatalogListingReq {
    private String keyword;
    private Boolean isInternal;
    private Boolean isCertificationCourse;
    private Boolean status;
    private String sortBy;
    private String sortType;
}
