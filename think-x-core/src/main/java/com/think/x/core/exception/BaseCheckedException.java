package com.think.x.core.exception;


/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 运行期异常基类
 * @author: GYW
 * @date: 2023/2/3 14:56
 * @version: v1.0
 */
public abstract class BaseCheckedException extends Exception implements BaseException {

    private static final long serialVersionUID = 2706069899924648586L;

    /**
     * 异常信息
     */
    protected String message;

    /**
     * 具体异常码
     */
    protected String code;

    public BaseCheckedException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseCheckedException(String code, String format, Object... args) {
        super(String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
