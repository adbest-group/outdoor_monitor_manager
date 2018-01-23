package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/20.
 */
public enum MonitorTaskType {
    UP_MONITOR(1,"上刊监测"),DURATION_MONITOR(2,"投放期间监测"),DOWNMONITOR(3,"下刊监测");

    private Integer id;
    private String text;

    MonitorTaskType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MonitorTaskType e : MonitorTaskType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MonitorTaskType e : MonitorTaskType.values()){
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
