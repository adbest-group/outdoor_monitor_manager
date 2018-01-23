package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/20.
 */
public enum JiucuoTaskStatus {
    UNVERIFY(1,"待审核"),VERIFIED(2,"通过审核"),VERIFY_FAILURE(3,"审核未通过");

    private Integer id;
    private String text;

    JiucuoTaskStatus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(JiucuoTaskStatus e : JiucuoTaskStatus.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(JiucuoTaskStatus e : JiucuoTaskStatus.values()){
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
