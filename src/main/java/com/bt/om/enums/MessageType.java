package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/19.
 */
public enum MessageType {
    ACTIVITY_AUDIT(1,"活动管理"),TASK_AUDIT(2,"任务审核"),TASK_ASSIGN(3,"任务指派"),JIUCUO_AUDIT(4,"纠错审核");

    private Integer id;
    private String text;

    MessageType(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MessageType e : MessageType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MessageType e : MessageType.values()){
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
