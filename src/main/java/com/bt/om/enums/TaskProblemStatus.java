package com.bt.om.enums;

/**
 * Created by caiting on 2018/2/2.
 */
public enum TaskProblemStatus {
    UNMONITOR(1,"未监测"),NO_PROBLEM(2,"无问题"),PROBLEM(3,"有问题"),FIXED(4,"已解决"),CLOSED(5,"已闭环");

    private Integer id;
    private String text;

    TaskProblemStatus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(TaskProblemStatus e : TaskProblemStatus.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(TaskProblemStatus e : TaskProblemStatus.values()){
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
