package com.course.server.exception;

public enum BusinessExceptionCode {

    MEMBER_NOT_EXIST("member does not exist"),
    USER_LOGIN_NAME_EXIST("username existed"),
    LOGIN_USER_ERROR("username not exist or password wrong"),
    LOGIN_MEMBER_ERROR("phone number does not exist or password wrong"),
    MOBILE_CODE_TOO_FREQUENT("too much SMS messages"),
    MOBILE_CODE_ERROR("SMS verification wrong"),
    MOBILE_CODE_EXPIRED("The SMS verification code does not exist or has expired, please resend the SMS"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
