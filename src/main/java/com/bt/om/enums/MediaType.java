package com.bt.om.enums;

/**
 * 媒体类型 枚举
 * 对应 ad_media_type 的 media_type
 */
public enum MediaType {
    PARENT_TYPE(1,"媒体大类"),
    SECOND_TYPE(2,"媒体小类");

    private Integer id;
    private String text;

    MediaType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MediaType e : MediaType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MediaType e : MediaType.values()){
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
