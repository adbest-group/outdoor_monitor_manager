package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/26.
 */
public enum SysUserExecuteType {
    WORKER(1,"监测人员"),CUSTOMER(2,"客户"),SOCIAL(3,"社会人员");

    private Integer id;
    private String text;

    SysUserExecuteType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(SysUserExecuteType e : SysUserExecuteType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(SysUserExecuteType e : SysUserExecuteType.values()){
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
