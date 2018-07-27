package com.bt.om.enums;

/**
 * APP用户类型枚举
 */
public enum AppUserTypeEnum {
    CUSTOMER(2,"广告主"),
    MEDIA(3,"媒体监测人员"),
    SOCIAL(4,"社会人员"),
    THIRD_COMPANY(5,"第三方监测人员");

    private Integer id;
    private String text;

    AppUserTypeEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AppUserTypeEnum e : AppUserTypeEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AppUserTypeEnum e : AppUserTypeEnum.values()){
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
