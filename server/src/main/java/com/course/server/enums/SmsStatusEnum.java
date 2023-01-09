package com.course.server.enums;

public enum SmsStatusEnum {

    USED("U", "have_used"),
    NOT_USED("N", "not_used");

    private String code;

    private String desc;

    SmsStatusEnum(String code, String desc) {
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
