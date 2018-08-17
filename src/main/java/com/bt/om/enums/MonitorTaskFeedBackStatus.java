package com.bt.om.enums;

/**
 * 监测任务反馈状态 枚举
 * 对应 ad_monitor_task 的 status
 */
public enum MonitorTaskFeedBackStatus {
	VALID_FEEDBACK(1, "反馈信息有效"),
	INVALID_FEEDBACK(2, "反馈信息无效");

    private Integer id;
    private String text;

    MonitorTaskFeedBackStatus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static String getText(int id) {
        for (MonitorTaskFeedBackStatus e : MonitorTaskFeedBackStatus.values()) {
            if (e.getId() == id) {
                return e.getText();
            }
        }
        return "";
    }

    public static Integer getId(String text) {
        for (MonitorTaskFeedBackStatus e : MonitorTaskFeedBackStatus.values()) {
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
