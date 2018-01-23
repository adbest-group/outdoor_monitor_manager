package com.bt.om.exception.web;

import com.bt.om.exception.ChainedRuntimeException;

/**
 * 没有权限的时候抛出的异常
 * 
 * @author hl-tanyong
 * @version $Id: PermissionException.java, v 0.1 2015年9月29日 下午2:53:37 hl-tanyong Exp $
 */
public class PermissionException extends ChainedRuntimeException {

    private static final long serialVersionUID = -2920004319471022222L;

    public PermissionException() {
        super();
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

}
