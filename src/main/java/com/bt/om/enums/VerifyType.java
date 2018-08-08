package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/20.
 * 审核类型
 */
public enum VerifyType {
	WAITVERITY_TYPE(0,"待审核"),
    PASS_TYPE(1,"通过"),
    REJECT_TYPE(2,"驳回");
	
    private Integer id;
    private String text;

    VerifyType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(VerifyType e : VerifyType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "待审核";
    }

    public static Integer getId(String text){
        for(VerifyType e : VerifyType.values()){
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
