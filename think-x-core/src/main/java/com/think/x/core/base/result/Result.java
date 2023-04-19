package com.think.x.core.base.result;


import com.think.x.core.base.params.IPage;
import com.think.x.core.base.params.PageData;
import com.think.x.core.exception.BaseExceptionCode;
import com.think.x.core.exception.BizException;
import io.vertx.core.json.Json;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @createTime 2023-02-22 10:55
 */
@Getter
@Setter
@SuppressWarnings("ALL")
@Accessors(chain = true)
public class Result<T> implements Serializable {
    public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍候再试";
    public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍候再试";
    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "-1";
    public static final String TIMEOUT_CODE = "-2";
    /**
     * 统一参数验证异常
     */
    public static final String VALID_EX_CODE = "-9";
    public static final String OPERATION_EX_CODE = "-10";
    /**
     * 调用是否成功标识，0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
     */
    private String code;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 结果消息，如果调用成功，消息通常为空T
     */
    private String msg = "ok";

    public Result() {

    }

    public Result(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;

    }

    public Result(String code, T data, String msg, boolean defExec) {
        this.code = code;
        this.data = data;
        this.msg = msg;

    }

    public static <E> Result<E> result(String code, E data, String msg) {
        return new Result<>(code, data, msg);
    }

    /**
     * 请求成功消息
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(SUCCESS_CODE, data, "ok");
    }

    public static Result<Boolean> success() {
        return new Result<>(SUCCESS_CODE, null, "ok");
    }

    public static <E> Result<E> successDef(E data) {
        return new Result<>(SUCCESS_CODE, data, "ok", true);
    }

    public static <E> Result<E> successDef() {
        return new Result<>(SUCCESS_CODE, null, "ok", true);
    }

    public static <E> Result<E> successDef(E data, String msg) {
        return new Result<>(SUCCESS_CODE, data, msg, true);
    }

    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data 结果
     * @param msg  消息
     * @return RPC调用结果
     */
    public static <E> Result<E> success(E data, String msg) {
        return new Result<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> Result<E> fail(String code, String msg) {
        return new Result<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> Result<E> fail(String msg) {
        return fail(OPERATION_EX_CODE, msg);
    }

    public static <E> Result<E> fail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new Result<>(OPERATION_EX_CODE, null, String.format(message, args));
    }

    public static <E> Result<E> fail(BaseExceptionCode exceptionCode) {
        return validFail(exceptionCode);
    }

    public static <E> Result<E> fail(BizException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new Result<>(exception.getCode(), null, exception.getMessage());
    }

    public static <E> Result<E> fail(E data, String msg) {
        return new Result<>(FAIL_CODE, data, msg);
    }


    public static <E> Result<E> fail(E data, BaseExceptionCode exceptionCode) {
        return new Result<>(exceptionCode.getCode(), data, exceptionCode.getMessage());
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static <E> Result<E> fail(Throwable throwable) {
        return fail(FAIL_CODE, throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE);
    }

    public static <E> Result<E> validFail(String msg) {
        return new Result<>(VALID_EX_CODE, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> Result<E> validFail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new Result<>(VALID_EX_CODE, null, String.format(message, args));
    }

    public static <E> Result<E> validFail(BaseExceptionCode exceptionCode) {
        return new Result<>(exceptionCode.getCode(), null,
                (exceptionCode.getMessage() == null || exceptionCode.getMessage().isEmpty()) ? DEF_ERROR_MESSAGE : exceptionCode.getMessage());
    }

    public static <E> Result<E> timeout() {
        return fail(TIMEOUT_CODE, HYSTRIX_ERROR_MESSAGE);
    }

    public static <E> Result<PageData<E>> pageDataResult(IPage page) {
        return Result.success(PageData.toPageData(page));
    }

    /**
     * 逻辑处理是否成功
     *
     * @return 是否成功
     */
    public boolean getIsSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }
    @Override
    public String toString() {
        return Json.encodePrettily(this);
    }
}
