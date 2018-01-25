package com.bt.om.enums;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: ResultCode.java, v 0.1 2015年9月29日 下午2:48:48 hl-tanyong Exp $
 */
public enum ResultCode {

    /** 参数错误**/
    RESULT_PARAM_ERROR(102), RESULT_NOAUTH(105),RESULT_NOLOGIN(103), RESULT_SUCCESS(100), RESULT_FAILURE(101),CLEAR_POSITION(109),ACTIVITY_TIME(111), NO_AUTHORITY_DELETE(501),NO_AUTHORITY_START(502);

    private ResultCode(int code) {
        this.code = code;
    }

    public String getCodeString() {
        return this.getCode() + "";
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}