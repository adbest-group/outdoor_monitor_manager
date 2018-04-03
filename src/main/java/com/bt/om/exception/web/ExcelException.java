package com.bt.om.exception.web;

import com.bt.om.exception.ChainedRuntimeException;

public class ExcelException extends ChainedRuntimeException {

	private static final long serialVersionUID = -2920004319471022222L;

    public ExcelException() {
        super();
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }
}
