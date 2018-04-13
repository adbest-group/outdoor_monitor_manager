package com.bt.om.vo.api;

/**
 * Created by caiting on 2018/4/10.
 * 获取短信验证码请求返回信息主体
 */
public class SMSCheckCodeResultVo {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
