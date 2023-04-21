package com.think.x.app;

import com.google.common.collect.Lists;
import com.think.x.app.verticle.HttpServerVerticle;
import com.think.x.core.json.JsonConfig;
import com.think.x.inject.GuiceIoc;
import com.think.x.inject.GuiceVertxDeploymentManager;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/31 16:02
 * @version: v1.0
 */
public class VertxAppLauncher {
    private static final Logger log = LoggerFactory.getLogger(VertxAppLauncher.class);
    private static final String PATH = "config/config.yaml";

    public static void main(String[] args) {

//        VertxOptions vertxOptions = new VertxOptions().setTracingOptions(new OpenTracingOptions(GlobalTracer.get()));
        Vertx vertx = Vertx.vertx();
        //依赖注入
        new GuiceIoc(vertx, Lists.newArrayList(MyVertxInjectModule.of()));

        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", PATH));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(store));
        retriever.getConfig(envConfig -> {
            if (envConfig.succeeded()) {
                log.info("config.yaml load succeeded.");
                JsonObject result = envConfig.result();
                JsonObject server = result.getJsonObject("server");
                Objects.requireNonNull(server, "server配置异常！");
                JsonObject dataSource = result.getJsonObject("dataSource");
                Objects.requireNonNull(dataSource, "dataSource配置异常！");
                //初始化jackson配置
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.config();
                deploymentManager.deployVerticle(HttpServerVerticle.class, new DeploymentOptions().setConfig(server), r -> {
                    if (r.succeeded()) {
                        log.info("Vertx application is up and running");
                    }
                    if (r.failed()) {
                        log.info("Vertx startup error,", r.cause());
                    }
                });
            } else {
                log.error("config.yaml load failed.");
            }
        });

    }

}
