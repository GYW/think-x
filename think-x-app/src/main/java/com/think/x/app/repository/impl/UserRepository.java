package com.think.x.app.repository.impl;

import com.google.inject.Singleton;
import com.think.x.app.domain.SysUser;
import com.think.x.app.repository.IUserRepository;
import com.think.x.core.base.params.PageData;
import com.think.x.core.base.params.PageParams;
import com.think.x.orm.repository.MyRepository;
import io.vertx.core.Future;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/11 11:40
 * @version: v1.0
 */
@Singleton
public class UserRepository extends MyRepository<SysUser, String> implements IUserRepository {

    public UserRepository() {
        super(SysUser.class);
    }

    public Future<SysUser> save(SysUser user) {
        return super.save(user);
    }

    public Future<SysUser> getOne(String id) {
        return super.getOne(id);
    }

    public Future<SysUser> getOneByName(String username) {
        return super.createQueryOne("from SysUser where username = :username", Map.of("username", username));
    }

    public Future<List<SysUser>> queryUsers() {
        return super.createQueryList("from SysUser", Map.of());
    }

    public Future<PageData<SysUser>> queryPageUsers(PageParams pageParams) {
        return super.queryPage("from SysUser", pageParams);
    }

    public Future<Integer> update(SysUser sysUser) {
        Map<String, ?> params = Map.of("mobile", sysUser.getMobile(), "id", sysUser.getId());
        return super.update("update SysUser set mobile =:mobile where id =:id", params);
    }

    public Future<Integer> deleteById(String id) {
        return super.delete(id);
    }


    public Future<Void> remove(SysUser sysUser) {
        return super.remove(sysUser);
    }


}

