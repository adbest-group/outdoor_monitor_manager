package com.bt.om.enums;

/**
 * 后台用户角色枚举
 */
public enum UserRoleEnum {
    ADMIN(100,"员工默认角色"),
    CUSTOMER(101,"广告主"),
    MEDIA(102,"媒体主"),
    SUPER_ADMIN(103,"超级管理员"),
    DEPARTMENT_LEADER(104,"部门领导默认角色"),
    ACTIVITY_ADMIN(105,"活动审核部员工"),
    TASK_ADMIN(106,"任务审核指派部员工"),
    JIUCUO_ADMIN(107,"纠错审核部员工"),
    ACTIVITY_LEADER(108,"活动审核部领导"),
    TASK_LEADER(109,"任务审核指派部领导"),
    JIUCUO_LEADER(110,"纠错审核部领导"),
    PHONE_OPERATOR(111,"话务中心人员"),
    THIRD_COMPANY(112,"第三方监测公司");

    private Integer id;
    private String text;

    UserRoleEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(UserRoleEnum e : UserRoleEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(UserRoleEnum e : UserRoleEnum.values()){
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
