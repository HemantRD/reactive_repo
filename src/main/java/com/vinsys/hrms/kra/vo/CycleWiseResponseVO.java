package com.vinsys.hrms.kra.vo;

public class CycleWiseResponseVO {

    private String cycleName;
    private Long count;

   
    public CycleWiseResponseVO() {
    }

   
    public CycleWiseResponseVO(String cycleName, Long count) {
        this.cycleName = cycleName;
        this.count = count;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
