package com.bt.om.enums;

/**
 * APP是否要更新 枚举
 */
public enum AppUpdateTypeEnum {
    FORCE_UPDATE(0,"有新版本,需要强制更新"),
    CAN_UPDATE(1,"有新版本,可去更新"),
    LATEEST_VERSION(2,"最新版本"),
    VERSION_ERROR(3,"版本号有误");

    private Integer id;
    private String text;

    AppUpdateTypeEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AppUpdateTypeEnum e : AppUpdateTypeEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AppUpdateTypeEnum e : AppUpdateTypeEnum.values()){
            if(e.getText().equals(text)){
                return e.getId();
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
