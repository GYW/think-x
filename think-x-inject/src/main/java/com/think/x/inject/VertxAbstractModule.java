package com.think.x.inject;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/13 15:47
 * @version: v1.0
 */
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class VertxAbstractModule extends AbstractModule {
    private GuiceIoc guiceIoc;
    private Vertx vertx;

    protected void configure() {
        bind(GuiceIoc.class).toInstance(this.guiceIoc);
        bind(Vertx.class).toInstance(this.vertx);
        bind(EventBus.class).toInstance(this.vertx.eventBus());
        bind(FileSystem.class).toInstance(this.vertx.fileSystem());
        bind(SharedData.class).toInstance(this.vertx.sharedData());
    }

}
