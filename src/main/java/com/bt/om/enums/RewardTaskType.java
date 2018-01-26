package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/26.
 */
public enum RewardTaskType {
    MONITOR(1,"监测任务"),JIUCUO(2,"纠错");

    private Integer id;
    private String text;

    RewardTaskType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(RewardTaskType e : RewardTaskType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(RewardTaskType e : RewardTaskType.values()){
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
