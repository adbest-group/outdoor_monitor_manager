package com.bt.om.enums;

/**
 * 群邑方批量导入广告位 枚举
 */
public enum MediaImportAdSeatEnum {
    ADSEAT_NAME(0, "广告位名称"),
    PARENT_NAME(1, "媒体大类"),
    SECOND_NAME(2, "媒体小类"),
    PROVINCE(3, "省（直辖市）"),
    CITY(4, "市"),
    ROAD(5, "主要路段"),
    LOCATION(6, "详细位置"),
    MEMO(7, "广告位编号"),
    LENGTH(8, "广告位长度"),
    WIDTH(9, "广告位宽度"),
    AD_NUM(10, "面数"),
    LON(11, "经度"),
    LAT(12, "纬度"),
    MAP_STANDARD(13, "地图标准（如百度，谷歌，高德）"),
    CONTACT_NAME(14, "联系人姓名"),
    CONTANT_CELL(15, "联系人电话"),
    IMPORT_RESULT(16, "导入结果"),
    IMPORT_DES(17, "导入错误信息");

    private Integer id;
    private String text;

    MediaImportAdSeatEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(MediaImportAdSeatEnum e : MediaImportAdSeatEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(MediaImportAdSeatEnum e : MediaImportAdSeatEnum.values()){
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
