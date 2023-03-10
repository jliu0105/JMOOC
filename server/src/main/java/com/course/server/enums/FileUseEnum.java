package com.course.server.enums;

public enum FileUseEnum {

    COURSE("C", "course"),
    TEACHER("T", "lecturer");

    private String code;

    private String desc;

    FileUseEnum(String code, String desc) {
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

    public static FileUseEnum getByCode(String code){
        for(FileUseEnum e: FileUseEnum.values()){
            if(code.equals(e.getCode())){
                return e;
            }
        }
        return  null;
    }
}
