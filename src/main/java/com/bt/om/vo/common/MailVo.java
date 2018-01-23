package com.bt.om.vo.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MailVo {

    /**
     * 发件人邮箱服务器
     */
    private String host;
    /**
     * 发件人邮箱
     */
    private String from;
    /**
     * 发件人用户名
     */
    private String account;
    /**
     * 发件人密码
     */
    private String password;
    /**
     * 收件人邮箱
     */
    private String[] to;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件中的图片，为空时无图片。map中的key为图片ID，value为图片地址
     */
    private Map<String, String> pictures = new HashMap<String,String>();
    /**
     * 邮件中的附件，为空时无附件。map中的key为附件ID，value为附件地址
     */
    private Map<String, String> attachments = new HashMap<String,String>();

    public String[] getTo() {
        return to;
    }
    public void setTo(String[] to) {
        this.to = to;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
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
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Map<String, String> getPictures() {
        return pictures;
    }
    public void setPictures(Map<String, String> pictures) {
        this.pictures = pictures;
    }
    public Map<String, String> getAttachments() {
        return attachments;
    }
    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }
    @Override
    public String toString() {
        return "MailVo [host=" + host + ", from=" + from + ", account=" + account + ", password="
               + password + ", to=" + Arrays.toString(to) + ", subject=" + subject + ", content="
               + content + "]";
    }
    
}
