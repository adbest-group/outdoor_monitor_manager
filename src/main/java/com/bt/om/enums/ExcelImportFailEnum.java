package com.bt.om.enums;

/**
 * Created by jiayong.mao on 2018/4/9.
 * 批量导入错误 枚举
 */
public enum ExcelImportFailEnum {
	ADNAME_NULL(1, "广告位名称不能为空"),
	PROVINCE_NULL(2, "所在省份不能为空"),
	CITY_NULL(3, "所在市不能为空"),
	REGION_NULL(4, "所在区不能为空"),
	STREET_NULL(5, "主要路段不能为空"),
	LOCATION_NULL(6, "详细地址不能为空"),
	SIZE_ONLYONE(7, "广告位尺寸不能只填写一个"),
	LOC_ONLYONE(8, "经纬度不能只填写一个"),
	LON_OVERFLOW(9, "经度不在-180到180之间"),
	LAT_OVERFLOW(10, "纬度不在-90到90之间"),
	NONE_MAP(11, "有经纬度没有地图标准"),
	LOC_DUP(12, "广告位地址重复"),
	PARENT_NULL(13, "媒体大类不能为空"),
	PARENT_INVALID(14, "媒体大类无效"),
	SECOND_NULL(15, "媒体小类不能为空"),
	SECOND_INVALID(16, "媒体小类无效"),
	MEDIA_TYPE_INVALID(17, "媒体类型无效"),
	UNIQUE_KEY_NULL(18, "该媒体类型唯一标识不能为空"),
	NUM_INVALID(19, "数字有误"),
	PROVINCE_INVALID(20, "省份/直辖市有误"),
	CITY_INVALID(21, "城市有误"),
	REGION_INVALID(22, "区/县有误"),
	STREET_INVALID(23, "主要路段有误"),
	MEDIA_NUM_INVAILD(24,"媒体方广告位编号不能为空"),
	MEDIA_NULL(25,"媒体主不能为空"),
	MEDIA_INVAILD(26,"媒体主名称无效"),
	MEMO_DUP(27,"广告位编号重复"),
	ADNAME_MEMO_NULL(28,"广告位编号不能为空"),
	TASK_USER_NULL(29,"任务执行人不能为空"),
	TASK_TYPE_NULL(30,"任务类型不能为空"),
	TASK_TYPE_INVALID(31,"任务类型不存在"),
	TASK_USER_INVALID(32,"任务执行人不存在"),
	TASK_VERIFY(33,"该任务已经通过审核"),
	PIC_INVALID(34,"图片不存在"),
	TASK_DUP(35,"任务重复"),
	LOC_INVALID(36,"该广告位没有参与本活动");
    private Integer id;
    private String text;

    ExcelImportFailEnum(int id,String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id){
        for(ExcelImportFailEnum e : ExcelImportFailEnum.values()){
            if(e.getId() == id){
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text){
        for(ExcelImportFailEnum e : ExcelImportFailEnum.values()){
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
