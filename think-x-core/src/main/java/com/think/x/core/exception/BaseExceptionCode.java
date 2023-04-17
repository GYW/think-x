package com.think.x.core.exception;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/2/3 14:56
 * @version: v1.0
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     *
     * @return
     */
    String getCode();

    /**
     * 异常消息
     *
     * @return
     */
    String getMessage();
}
