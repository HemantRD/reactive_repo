package com.vinsys.hrms.idp.trainingcatalog.vo;

public class CompetencyDDLVo {
    private Integer id;
    private String value;

    public CompetencyDDLVo() {
    }

    public CompetencyDDLVo(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}