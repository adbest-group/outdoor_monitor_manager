package com.bt.om.exception;

/**
 * 代表一个实体类的异常
 * 
 * @author tany
 * @version create on 2011-5-25 上午11:32:11
 */
public class EntityException extends ChainedRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -8397043650640226361L;

    /**
     * 构造一个空的异常.
     */
    public EntityException() {
        super();
    }

    /**
     * 构造一个异常, 指明异常的详细信息.
     * 
     * @param message
     *            详细信息
     */
    public EntityException(String message) {
        super(message);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     * 
     * @param cause
     *            异常的起因
     */
    public EntityException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     * 
     * @param message
     *            详细信息
     * @param cause
     *            异常的起因
     */
    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

}
