package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/31.
 */
public enum AgePart {
    LT18(1, "<18岁"), P18_24(2, "18-24"), P25_29(3, "25-29"), P30_39(4, "30-39"), P40_49(5, "40-49"), P50_59(6, "50-59"), GT60(7, "60以上");

    private Integer id;
    private String text;

    AgePart(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id) {
        for (AgePart e : AgePart.values()) {
            if (e.getId() == id) {
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text) {
        for (AgePart e : AgePart.values()) {
            if (e.getText().equals(text)) {
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
