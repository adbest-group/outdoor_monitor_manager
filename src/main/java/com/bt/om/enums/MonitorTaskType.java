package com.bt.om.enums;

/**
 * 监测任务类型 枚举
 * 对应 ad_monitor_task 的 task_type
 */
public enum MonitorTaskType {
    //SET_UP_MONITOR(5,"上刊安装"),
	UP_TASK(5,"上刊"),
    UP_MONITOR(1,"上刊监测"),
    DURATION_MONITOR(2,"投放期间监测"),
    DOWNMONITOR(3,"下刊监测"),
    ZHUIJIA_MONITOR(6,"追加监测"),
    FIX_CONFIRM(4,"复查监测");

    private Integer id;
    private String text;

    MonitorTaskType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MonitorTaskType e : MonitorTaskType.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MonitorTaskType e : MonitorTaskType.values()){
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
