package com.think.x.app.verticle;

import com.think.x.app.config.CommonConstants;
import com.think.x.app.config.DataSourceConfig;
import com.think.x.app.config.HttpServerConfig;
import com.think.x.core.json.JsonConfig;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MainVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);
    private static final String PATH = "config/config.yaml";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        ConfigStoreOptions store = new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", PATH));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(store));
        retriever.getConfig(envConfig -> {
            if (envConfig.succeeded()) {
                log.info("config.yaml load succeeded.");
                JsonObject result = envConfig.result();
                JsonObject server = result.getJsonObject("server");
                Objects.requireNonNull(server, "server配置异常！");
                JsonObject dataSource = result.getJsonObject("dataSource");
                Objects.requireNonNull(dataSource, "dataSource配置异常！");
                SharedData sharedData = vertx.sharedData();
                LocalMap<String, Object> localMap = sharedData.getLocalMap(CommonConstants.CONFIG);
                HttpServerConfig serverConfig = server.mapTo(HttpServerConfig.class);
                localMap.put(CommonConstants.CONFIG_SERVER, serverConfig);
                DataSourceConfig dataSourceConfig = dataSource.mapTo(DataSourceConfig.class);
                localMap.put(CommonConstants.CONFIG_DATASOURCE, dataSourceConfig);
                //加载配置类
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.config();
            } else {
                log.error("config.yaml load failed.");
            }
        });
    }

}
