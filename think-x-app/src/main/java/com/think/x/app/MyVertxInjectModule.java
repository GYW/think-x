package com.think.x.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.think.x.app.api.UserApi;
import com.think.x.app.repository.IUserRepository;
import com.think.x.app.repository.impl.UserRepository;
import com.think.x.core.base.id.SnowFlakeIdWorker;
import com.think.x.core.exception.GlobalExceptionHandler;
import com.think.x.web.routes.CustomAbstractRoute;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import lombok.AllArgsConstructor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.persistence.Persistence;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 依赖注入类bind
 * @author: GYW
 * @date: 2023/4/12 16:39
 * @version: v1.0
 */
@AllArgsConstructor(staticName = "of")
public class MyVertxInjectModule extends AbstractModule {

    protected void configure() {
        this.bind(IUserRepository.class).to(UserRepository.class).asEagerSingleton();
        this.bind(CustomAbstractRoute.class).to(UserApi.class);
        //TODO:: api class should be bindable CustomAbstractRoute
    }

    @Provides
    @Singleton
    public Mutiny.SessionFactory providesSessionFactory() {
        //初始化 hibernate sessionFactory
        return Persistence.createEntityManagerFactory("think-x").unwrap(Mutiny.SessionFactory.class);
    }

    @Provides
    @Singleton
    public Router router(Vertx vertx) {
        Router mainRouter = Router.router(vertx);
        mainRouter.route().handler(BodyHandler.create());
        mainRouter.route().handler(ResponseContentTypeHandler.create());
        //统一异常处理
        mainRouter.route().last().failureHandler(GlobalExceptionHandler.of());
        return mainRouter;
    }

    /**
     * 雪花算法实现ID策略
     *
     * @return
     */
    @Provides
    @Singleton
    public IdentifierGenerator identifierGenerator() {
        return new SnowFlakeIdWorker();
    }

    @Provides
    @Singleton
    public SchemaParser schemaParser(Vertx vertx) {
        return SchemaParser.createDraft7SchemaParser(SchemaRouter.create(vertx, new SchemaRouterOptions()));
    }
}
