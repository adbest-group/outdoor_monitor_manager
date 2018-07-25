package com.bt.om.enums;

/**
 * 是否允许多个活动 枚举类
 */
public enum AllowMultiEnum {
    ALLOW(1,"是"),NOT_ALLOW(0,"否");

    private Integer id;
    private String text;

    AllowMultiEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AllowMultiEnum e : AllowMultiEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AllowMultiEnum e : AllowMultiEnum.values()){
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
