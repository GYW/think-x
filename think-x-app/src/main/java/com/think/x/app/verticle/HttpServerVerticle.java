package com.think.x.app.verticle;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.think.x.app.config.HttpServerConfig;
import com.think.x.app.api.UserApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/13 12:37
 * @version: v1.0
 */
@Singleton
public class HttpServerVerticle extends AbstractVerticle {

    Logger log = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Inject
    private Injector injector;

    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer httpServer = vertx.createHttpServer();
        UserApi instance = injector.getInstance(UserApi.class);
        //初始化路由
        instance.init();
        httpServer.requestHandler(instance.getRouter());
        HttpServerConfig httpServerConfig = config().mapTo(HttpServerConfig.class);
        httpServer.listen(httpServerConfig.getPort(), http -> {
            if (http.succeeded()) {
                log.info("HTTP server [{}] started on port {} .", httpServerConfig.getContextPath(), httpServerConfig.getPort());
                startPromise.complete();
            } else {
                log.info("HTTP server [{}] failed on port {} .", httpServerConfig.getContextPath(), httpServerConfig.getPort());
                startPromise.fail(http.cause());
            }
        });

    }
}
