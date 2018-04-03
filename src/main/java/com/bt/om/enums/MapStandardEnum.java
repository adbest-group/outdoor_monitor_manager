package com.bt.om.enums;

/**
 * Created by jiayong.mao on 2018/4/2.
 */
public enum MapStandardEnum {
    UNCONFIRM(1,"百度"),CONFIRMED(2,"谷歌"),COMPLETE(3,"高德");

    private Integer id;
    private String text;

    MapStandardEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MapStandardEnum e : MapStandardEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MapStandardEnum e : MapStandardEnum.values()){
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
