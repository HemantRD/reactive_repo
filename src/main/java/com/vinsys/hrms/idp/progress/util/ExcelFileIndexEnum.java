package com.vinsys.hrms.idp.progress.util;

public enum ExcelFileIndexEnum {
    MEMBER_EMAIL(0, "MemberEmail"),
    TRAINING_CODE(1, "TrainingCode"),
    PROGRESS_DATE(2, "ProgressDate"),
    PROGRESS_VALUE(3, "ProgressValue"),
    PROGRESS_UNIT(4, "ProgressUnit"),
    REMARK(5, "Remark"),
    STATUS(6, "Status");

    int index;
    String name;
    String type;

    ExcelFileIndexEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
