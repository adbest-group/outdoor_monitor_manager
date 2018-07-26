package com.bt.om.enums;

/**
 * Created by caiting on 2018/4/11.
 */
public enum UserExecuteType {
    MONITOR(1,"监测人员"),
    CUSTOMER(2,"客户人员"),
    MEDIA_WORKER(3,"媒体人员"),
    Social(4,"社会人员"),
    THIRD_COMPANY(5,"第三方监测人员");

    private Integer id;
    private String text;

    UserExecuteType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(UserExecuteType e : UserExecuteType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(UserExecuteType e : UserExecuteType.values()){
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
