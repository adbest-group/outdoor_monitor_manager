package com.bt.om.enums;

/**
 * 积分设置枚举
 * 对应 ad_point 的type
 */
public enum AdPointEnum {
    INVITE_REGIST(1,"邀请码注册"),
    NORMAL_REGIST(9,"正常注册");

    private Integer id;
    private String text;

    AdPointEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AdPointEnum e : AdPointEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AdPointEnum e : AdPointEnum.values()){
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
