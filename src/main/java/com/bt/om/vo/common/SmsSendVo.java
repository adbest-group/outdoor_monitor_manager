package com.bt.om.vo.common;

public class SmsSendVo {
    
    private String userid;         //"25008"
    private String account;        //"hz-adtime"
    private String password;       //"adtime.com"

    private String mobile;         //13429629817 多个号码以半角逗号分隔
    private String content;        //文字内容
    private String sendTime;       //""立即发送 
    private String action;         //固定"send" 

    private String checkcontent;   //"0" 是否检查内容包含非法关键字
    private String taskName;       //任务名称
    private String countnumber;    //号码总数量
    private String mobilenumber;   //手机号码数量
    private String telephonenumber; //小灵通或座机号码数
    
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getCheckcontent() {
        return checkcontent;
    }
    public void setCheckcontent(String checkcontent) {
        this.checkcontent = checkcontent;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getCountnumber() {
        return countnumber;
    }
    public void setCountnumber(String countnumber) {
        this.countnumber = countnumber;
    }
    public String getMobilenumber() {
        return mobilenumber;
    }
    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }
    public String getTelephonenumber() {
        return telephonenumber;
    }
    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }
    @Override
    public String toString() {
        return "SmsSendVo [userid=" + userid + ", account=" + account + ", password=" + password
               + ", mobile=" + mobile + ", content=" + content + ", sendTime=" + sendTime
               + ", action=" + action + ", checkcontent=" + checkcontent + ", taskName=" + taskName
               + ", countnumber=" + countnumber + ", mobilenumber=" + mobilenumber
               + ", telephonenumber=" + telephonenumber + "]";
    }
    
}
