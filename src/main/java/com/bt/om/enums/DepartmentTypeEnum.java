package com.bt.om.enums;

/**
 * 部门类型 枚举
 * 对应 sys_resources 的 department_type
 */
public enum DepartmentTypeEnum {
    ACTIVITY(1,"活动审核部门"),
    MONITOR_TASK(2,"任务审核指派部门"),
    JIUCUO_TASK(3,"纠错审核部门");

    private Integer id;
    private String text;

    DepartmentTypeEnum(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(DepartmentTypeEnum e : DepartmentTypeEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(DepartmentTypeEnum e : DepartmentTypeEnum.values()){
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
