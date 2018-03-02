package com.bt.om.vo.api;

import java.io.Serializable;

/**
 * Created by caiting on 2018/3/1.
 */
public class ImageCodeResultVo implements Serializable{
    private String token;
    private String image_code_base64;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage_code_base64() {
        return image_code_base64;
    }

    public void setImage_code_base64(String image_code_base64) {
        this.image_code_base64 = image_code_base64;
    }
}
