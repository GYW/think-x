package com.think.x.core.exception;


/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 非运行期异常基类，所有自定义非运行时异常继承该类
 * @author: GYW
 * @date: 2023/2/3 14:56
 * @version: v1.0
 */
public class BaseUncheckedException extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -778887391066124051L;

    /**
     * 异常信息
     */
    protected String message;

    /**
     * 具体异常码
     */
    protected String code;

    public BaseUncheckedException(Throwable cause) {
        super(cause);
    }

    public BaseUncheckedException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }


    public BaseUncheckedException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(String code, String format, Object... args) {
        super(String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
