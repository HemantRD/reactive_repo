package com.vinsys.hrms.entity.idp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_idp_competency_types")
public class CompetencyTypes {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "record_status", nullable = false)
    private Boolean recordStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Boolean recordStatus) {
        this.recordStatus = recordStatus;
    }
}