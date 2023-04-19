package com.think.x.app.api;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.think.x.app.domain.SysUser;
import com.think.x.app.repository.IUserRepository;
import com.think.x.core.base.params.PageData;
import com.think.x.core.base.params.PageParams;
import com.think.x.core.base.result.Result;
import com.think.x.web.routes.CustomAbstractRoute;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.RequestPredicate;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.vertx.json.schema.common.dsl.Keywords.maxLength;
import static io.vertx.json.schema.common.dsl.Keywords.minLength;
import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/13 17:32
 * @version: v1.0
 */
@Slf4j
@Singleton
public class UserApi extends CustomAbstractRoute {
    @Inject
    @Getter
    public Router router;

    @Inject
    @Getter
    IUserRepository userRepository;
    @Inject
    private SchemaParser schemaParser;

    @Override
    public void init() {
        queryPage();
        queryUserInfo();
        queryUsers();
        saveUser();
        //TODO:: 增加API路由
    }

    /**
     * 分页查询分页列表
     */
    private void queryPage() {
        router.get("/api/user/pages").handler(ctx -> {
            Future<PageData<SysUser>> pages = userRepository.queryPageUsers(new PageParams());
            pages.onSuccess(pageData -> {
                ctx.response().end(Result.pageDataResult(pageData).toString());
            }).onFailure(this.failureThrowableHandler(ctx));
        });
    }

    /**
     * 查询用户信息
     */
    public void queryUserInfo() {
        ObjectSchemaBuilder bodySchema = objectSchema()
                .requiredProperty("username", stringSchema().with(minLength(1)).with(maxLength(100)).nullable());
        router.get("/api/user/info")
                .handler(ValidationHandlerBuilder.create(schemaParser)
                        .predicate(RequestPredicate.BODY_REQUIRED)
                        .body(Bodies.json(bodySchema))
                        .build())
                .handler(ctx -> {
                    RequestBody body = valid(ctx);
                    JsonObject params = body.asJsonObject();
                    String username = params.getString("username");
                    Future<SysUser> one = userRepository.getOneByName(username);
                    one.onSuccess(sysUser -> {
                        ctx.response().end(Result.success(sysUser).toString());
                    }).onFailure(this.failureThrowableHandler(ctx));
                });
    }


    public void queryUsers() {
        router.get("/api/user/list").handler(ctx -> {
            valid(ctx);
            Future<List<SysUser>> listFuture = userRepository.queryUsers();
            listFuture.onSuccess(users -> {
                ctx.response().end(Result.success(users).toString());
            }).onFailure(this.failureThrowableHandler(ctx));
        });
    }

    public void saveUser() {
        router.post("/api/user/save").handler(ctx -> {
            RequestBody body = valid(ctx);
            SysUser sysUser = body.asPojo(SysUser.class);
            Future<SysUser> sysUserFuture = userRepository.save(sysUser);
            sysUserFuture.onSuccess(users -> {
                ctx.response().end(Result.success().toString());
            }).onFailure(this.failureThrowableHandler(ctx));
        });
    }
}
