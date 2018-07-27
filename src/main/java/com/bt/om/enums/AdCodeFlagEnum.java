package com.bt.om.enums;

/**
 * 是否贴上二维码 枚举
 * 对应 ad_seat_info 的 ad_code_flag
 */
public enum AdCodeFlagEnum {
    YES(1, "已贴上"),
    NO(0, "没贴上");

    private Integer id;
    private String text;

    AdCodeFlagEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AdCodeFlagEnum e : AdCodeFlagEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AdCodeFlagEnum e : AdCodeFlagEnum.values()){
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
