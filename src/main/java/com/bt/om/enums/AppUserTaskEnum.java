package com.bt.om.enums;

/**
 * APP人员执行任务的状态
 * 对应 ad_monitor_user_task 的 status
 */
public enum AppUserTaskEnum {
    NORMAL(1, "正常"),
    ABANDON(2, "主动放弃"),
    RECYCLE(3, "超时回收"),
    CHANGE(4, "修改指派");

    private Integer id;
    private String text;

    AppUserTaskEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AppUserTaskEnum e : AppUserTaskEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AppUserTaskEnum e : AppUserTaskEnum.values()){
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
