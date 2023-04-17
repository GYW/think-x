package com.think.x.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import lombok.Getter;

import java.util.List;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/13 14:54
 * @version: v1.0
 */
@Getter
public class GuiceIoc {

    private static GuiceIoc instance;
    private static Injector injector;

    public GuiceIoc(Vertx vertx, List<AbstractModule> modules) {
        instance = this;
        try {
            modules.add(VertxAbstractModule.of(this, vertx));
            injector = Guice.createInjector(modules);
            injector.injectMembers(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        /**
         * verx
         */
        vertx.registerVerticleFactory(new GuiceVerticleFactory(injector));

    }

    public static <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

}
