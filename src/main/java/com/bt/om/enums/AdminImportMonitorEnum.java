package com.bt.om.enums;

/**
 * 群邑方批量导入监测任务 枚举
 */
public enum AdminImportMonitorEnum {
    ADSEAT_MEMO(0, "广告位编号"),
    TASK_USER(1, "任务执行人"),
    TASK_TYPE(2, "任务类型"),
    IMPORT_RESULT(3, "导入结果"),
    IMPORT_DES(4, "导入错误信息");

    private Integer id;
    private String text;

    AdminImportMonitorEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AdminImportMonitorEnum e : AdminImportMonitorEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AdminImportMonitorEnum e : AdminImportMonitorEnum.values()){
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
