package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/26.
 */
public enum RewardType {
    ADD(1,"奖励"),REDUCE(2,"兑换");

    private Integer id;
    private String text;

    RewardType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(RewardType e : RewardType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(RewardType e : RewardType.values()){
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
