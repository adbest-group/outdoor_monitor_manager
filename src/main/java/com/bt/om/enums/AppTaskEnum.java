package com.bt.om.enums;

/**
 * APP执行的任务类型
 */
public enum AppTaskEnum {
    MONITOR_TASK(1, "监测任务"),
    JIUCUO_TASK(2, "纠错任务");

    private Integer id;
    private String text;

    AppTaskEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AppTaskEnum e : AppTaskEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AppTaskEnum e : AppTaskEnum.values()){
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
