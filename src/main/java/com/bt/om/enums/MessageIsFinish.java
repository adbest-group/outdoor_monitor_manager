package com.bt.om.enums;

/**
 * 站内信是否已处理 枚举
 * 对应 sys_user_message 的 is_finish
 */
public enum MessageIsFinish {
    CONFIRMED(0,"未处理"),UNCONFIRM(1,"已处理");

    private Integer id;
    private String text;

    MessageIsFinish(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MessageIsFinish e : MessageIsFinish.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MessageIsFinish e : MessageIsFinish.values()){
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
