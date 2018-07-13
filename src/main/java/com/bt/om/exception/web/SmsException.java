package com.bt.om.exception.web;

import com.bt.om.exception.ChainedRuntimeException;

public class SmsException extends ChainedRuntimeException {

	private static final long serialVersionUID = -2920004319471022222L;

    public SmsException() {
        super();
    }

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsException(String message) {
        super(message);
    }

    public SmsException(Throwable cause) {
        super(cause);
    }
}
