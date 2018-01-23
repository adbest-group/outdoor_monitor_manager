package com.bt.om.vo.web;

import com.bt.om.entity.ID;
import com.bt.om.enums.ResultCode;

/**
 * 统一返回的对象
 * 
 * @author hl-tanyong
 * @version $Id: ResultVo.java, v 0.1 2015年9月29日 下午2:47:04 hl-tanyong Exp $
 */
public class ResultVo<T> extends ID {
    private static final long serialVersionUID = -1222614520893986846L;

    private T result;

    /**
     * 请求处理结果
     */
    private int code = ResultCode.RESULT_SUCCESS.getCode();

    /**
     * 请求处理结果描述
     */
    private String resultDes;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResultDes() {
        return resultDes;
    }

    public void setResultDes(String resultDes) {
        this.resultDes = resultDes;
    }
}
