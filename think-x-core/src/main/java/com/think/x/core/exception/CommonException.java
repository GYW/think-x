package com.think.x.core.exception;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:  * 非业务异常：用于在处理非业务逻辑时，进行抛出的异常。
 * @author: GYW
 * @date: 2023/2/3 14:56
 * @version: v1.0
 */
public class CommonException extends BaseCheckedException {


    public CommonException(String code, String message) {
        super(code, message);
    }

    public CommonException(String code, String format, Object... args) {
        super(code, String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }


    public CommonException wrap(String code, String format, Object... args) {
        return new CommonException(code, format, args);
    }

    @Override
    public String toString() {
        return "BizException [message=" + message + ", code=" + code + "]";
    }
}
