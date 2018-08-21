package com.bt.om.enums;

/**
 * 后台用户类型枚举
 * 对应 sys_user 的 usertype
 */
public enum UserTypeEnum {
    ADMIN(1,"普通员工"),
    CUSTOMER(2,"广告主"),
    MEDIA(3,"媒体主"),
    SUPER_ADMIN(4,"超级管理员"),
    DEPARTMENT_LEADER(5,"部门领导"),
    PHONE_OPERATOR(6,"话务员"),
    THIRD_COMPANY(7,"第三方监测公司");

    private Integer id;
    private String text;

    UserTypeEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(UserTypeEnum e : UserTypeEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(UserTypeEnum e : UserTypeEnum.values()){
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
