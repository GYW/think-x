package com.think.x.web.routes;

import com.think.x.core.base.result.Result;
import io.vertx.core.Handler;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/14 15:09
 * @version: v1.0
 */
@Slf4j
public abstract class CustomAbstractRoute {

    /**
     * 异常处理
     *
     * @param ctx
     * @return
     */
    @NotNull
    protected Handler<Throwable> failureThrowableHandler(RoutingContext ctx) {
        return throwable -> {
            //输出异常日志
            log.error(throwable.getMessage());
            //响应失败信息
            ctx.response().end(throwable.getMessage());
        };
    }


    @NotNull
    protected RequestBody valid(RoutingContext ctx) {
        RequestBody body = ctx.body();
        if (body.isEmpty()) {
            ctx.response().end(Result.fail("参数不能为空").toString());
        }
        return body;
    }

    public abstract void init();
}
