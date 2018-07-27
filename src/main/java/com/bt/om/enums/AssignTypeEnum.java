package com.bt.om.enums;

/**
 * 用户获取任务 枚举
 * 对应 ad_monitor_user_task 的assign_type
 */
public enum AssignTypeEnum {
    ASSIGN(1,"系统指派"),
    SOCIAL_GRAB(2,"自主抢单（社会人员）"),
    MEDIA_GRAB(3,"自主接取（媒体监测人员）");

    private Integer id;
    private String text;

    AssignTypeEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AssignTypeEnum e : AssignTypeEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AssignTypeEnum e : AssignTypeEnum.values()){
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
