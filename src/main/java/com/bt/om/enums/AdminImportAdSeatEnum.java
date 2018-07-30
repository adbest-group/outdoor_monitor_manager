package com.bt.om.enums;

/**
 * 群邑方批量导入广告位 枚举
 */
public enum AdminImportAdSeatEnum {
    ADSEAT_NAME(0, "广告位名称"),
    MEDIA_NAME(1, "所属媒体主"),
    PARENT_NAME(2, "媒体大类"),
    SECOND_NAME(3, "媒体小类"),
    PROVINCE(4, "省（直辖市）"),
    CITY(5, "市"),
    ROAD(6, "主要路段"),
    LOCATION(7, "详细位置"),
    MEMO(8, "广告位编号"),
    LENGTH(9, "广告位长度"),
    WIDTH(10, "广告位宽度"),
    AD_NUM(11, "面数"),
    LON(12, "经度"),
    LAT(13, "纬度"),
    MAP_STANDARD(14, "地图标准（如百度，谷歌，高德）"),
    CONTACT_NAME(15, "联系人姓名"),
    CONTANT_CELL(16, "联系人电话"),
    IMPORT_RESULT(17, "导入结果"),
    IMPORT_DES(18, "导入错误信息");

    private Integer id;
    private String text;

    AdminImportAdSeatEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(AdminImportAdSeatEnum e : AdminImportAdSeatEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(AdminImportAdSeatEnum e : AdminImportAdSeatEnum.values()){
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
