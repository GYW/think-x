package com.think.x.core.exception;

import com.think.x.core.base.result.Result;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 全局统一异常处理
 * @author: GYW
 * @date: 2023/4/17 15:15
 * @version: v1.0
 */
@Slf4j
public class GlobalExceptionHandler implements Handler<RoutingContext> {
    private GlobalExceptionHandler() {
    }

    @Override
    public void handle(RoutingContext ctx) {
        Throwable throwable = ctx.failure();
        log.error("Global exception fail: ", throwable);
        ctx.json(Result.fail(throwable.getMessage()));
    }

    public static GlobalExceptionHandler of() {
        return new GlobalExceptionHandler();
    }
}
