package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/19.
 */
public enum ActivityStatus {
    UNCONFIRM(1,"未确认"),CONFIRMED(2,"已确认"),COMPLETE(3,"已完成");

    private Integer id;
    private String text;

    ActivityStatus(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(ActivityStatus e : ActivityStatus.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(ActivityStatus e : ActivityStatus.values()){
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
