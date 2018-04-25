package com.bt.om.enums;

/**
 * Created by jiayong.mao on 2018/4/19.
 */
public enum AdMediaInfoStatus {
    NOT_BEGIN(1, "未开始"),WATCHING(2, "监测中"),HAS_PROBLEM(3, "有问题"),FINISHED(4, "已结束");

    private Integer id;
    private String text;

    AdMediaInfoStatus(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AdMediaInfoStatus e : AdMediaInfoStatus.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AdMediaInfoStatus e : AdMediaInfoStatus.values()){
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
