package com.vinsys.hrms.entity.idp;

import com.vinsys.hrms.entity.AuditBase;

import javax.persistence.*;

@Entity
@Table(name = "tbl_mst_idp_training_cateloge_keywords")
public class TrainingCatalogKeywords {

    @Id
    @SequenceGenerator(name = "seq_mst_idp_training_cateloge_keywords",
            sequenceName = "seq_mst_idp_training_cateloge_keywords", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_idp_training_cateloge_keywords")
    private Long id;

    @Column(name = "training_id", length = 50, nullable = false)
    private Long trainingId;

    @Column(name = "keyword", length = 50, nullable = false)
    private String keyword;

    @Column(name = "record_status", nullable = false)
    private Boolean recordStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Boolean recordStatus) {
        this.recordStatus = recordStatus;
    }
}
