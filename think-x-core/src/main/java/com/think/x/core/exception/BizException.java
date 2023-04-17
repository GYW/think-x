package com.think.x.core.exception;


import com.think.x.core.base.result.Result;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: * 业务异常： 用于在处理业务逻辑时，进行抛出的异常
 * @author: GYW
 * @date: 2023/2/3 14:56
 * @version: v1.0
 */
public class BizException extends BaseUncheckedException {

    private static final long serialVersionUID = -3843907364558373817L;

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BizException(String message) {
        super("-1", message);
    }

    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String code, String message, Object... args) {
        super(code, message, args);
    }

    public BizException(BaseExceptionCode baseExceptionCode) {
        super(baseExceptionCode.getCode(), baseExceptionCode.getMessage());
    }


    /**
     * 实例化异常
     *
     * @param code    自定义异常编码
     * @param message 自定义异常消息
     * @param args    已定义异常参数
     * @return
     */
    public static BizException wrap(String code, String message, Object... args) {
        return new BizException(code, message, args);
    }

    public static BizException wrap(String message, Object... args) {
        return new BizException("-1", message, args);
    }

    public static BizException validFail(String message, Object... args) {
        return new BizException("-9", message, args);
    }

    public static BizException wrap(BaseExceptionCode ex) {
        return new BizException(ex.getCode(), ex.getMessage());
    }

    /**
     * 根据api返回结果构建异常
     *
     * @param result
     * @return
     */
    public static BizException wrap(Result result) {
        return new BizException(result.getCode(), result.getMsg());
    }

    @Override
    public String toString() {
        return "BizException [message=" + message + ", code=" + code + "]";
    }

}
