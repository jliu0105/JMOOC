package com.course.server.enums;

public enum CourseLevelEnum {

    ONE("1", "entry_level"),
    TWO("2", "mid_level"),
    THREE("3", "high_level");

    private String code;

    private String desc;

    CourseLevelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
