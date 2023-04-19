package com.think.x.app.repository;

import com.think.x.app.domain.SysUser;
import com.think.x.core.base.params.PageData;
import com.think.x.core.base.params.PageParams;
import io.vertx.core.Future;

import java.util.List;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/11 11:43
 * @version: v1.0
 */
public interface IUserRepository {
    Future<SysUser> save(SysUser user);

    Future<SysUser> getOne(String id);

    Future<SysUser> getOneByName(String username);

    Future<List<SysUser>> queryUsers();

    Future<PageData<SysUser>> queryPageUsers(PageParams pageParams);
}
