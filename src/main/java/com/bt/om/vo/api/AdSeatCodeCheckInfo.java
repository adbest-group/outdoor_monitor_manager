package com.bt.om.vo.api;

/**
 * Created by caiting on 2018/3/23.
 */
public class AdSeatCodeCheckInfo {
    private boolean valid;
    private String err_msg;

    public AdSeatCodeCheckInfo(){
        this.valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }
}
