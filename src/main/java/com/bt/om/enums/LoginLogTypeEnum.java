package com.bt.om.enums;

/**
 * 登录日志类型 枚举 
 * 对应 login_log 表的 type
 */
public enum LoginLogTypeEnum {
    PLATFORM(0,"后台"),
    APP(1,"APP");

    private Integer id;
    private String text;

    LoginLogTypeEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(LoginLogTypeEnum e : LoginLogTypeEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(LoginLogTypeEnum e : LoginLogTypeEnum.values()){
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
