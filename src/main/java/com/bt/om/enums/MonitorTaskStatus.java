package com.bt.om.enums;

/**
 * Created by caiting on 2018/1/20.
 */
public enum MonitorTaskStatus {
    UN_ACTIVE(7, "待激活"),
    CAN_GRAB(8, "可抢单"),
    UNASSIGN(1, "待指派"),
    TO_CARRY_OUT(2, "待执行"),
    UN_FINISHED(6, "未完成"),
    UNVERIFY(3, "待审核"),
    VERIFIED(4, "审核通过"),
    VERIFY_FAILURE(5, "审核未通过");

    private Integer id;
    private String text;

    MonitorTaskStatus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id) {
        for (MonitorTaskStatus e : MonitorTaskStatus.values()) {
            if (e.getId() == id) {
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text) {
        for (MonitorTaskStatus e : MonitorTaskStatus.values()) {
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
