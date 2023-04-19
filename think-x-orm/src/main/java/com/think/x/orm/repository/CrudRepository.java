package com.think.x.orm.repository;

import com.think.x.core.base.domain.BaseEntity;
import com.think.x.core.base.params.PageData;
import com.think.x.core.base.params.PageParams;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: repository基类
 * @author: GYW
 * @date: 2023/4/7 14:56
 * @version: v1.0
 */
public interface CrudRepository<T extends BaseEntity<E>, E extends Serializable> {
    @Nullable
    <T> Future withTransaction(@NotNull BiFunction<Mutiny.Session, Mutiny.Transaction, Uni<T>> execution);

    @Nullable
    Future<T> save(@NotNull T entity);

    @Nullable
    Future<Boolean> remove(@NotNull T entity);

    @Nullable
    Future<Boolean> batchSave(@NotNull Collection<T> entities);

    @Nullable
    Future<Boolean> delete(@Nullable E id);

    @Nullable
    Future<Boolean> exists(@Nullable E id);

    @Nullable
    Future<T> getOne(@Nullable E id);

    /**
     * 返回对象集合
     *
     * @param sql
     * @param params
     * @return
     */
    @Nullable
    Future<List<T>> createQueryList(@NotNull String sql, Map<String, ?> params);

    /**
     * 返回单个对象
     *
     * @param sql
     * @param params
     * @return
     */
    @Nullable
    Future<T> createQueryOne(@NotNull String sql, @NotNull Map<String, ?> params);

    /**
     * 执行 sql
     *
     * @param sql
     * @param params
     * @return
     */
    @Nullable
    Future<Integer> execute(@NotNull String sql, @NotNull Map<String, ?> params);

    @Nullable
    Future<T> createQuery(BiFunction<Mutiny.Session, Mutiny.Transaction, Uni<T>> execution);

    @Nullable
    Future<Long> queryPageCount(@NotNull String sql, @NotNull PageParams params);

    @Nullable
    Future<List<T>> queryPageResult(@NotNull String sql, @NotNull PageParams params);

    @Nullable
    Future<PageData<T>> queryPage(@NotNull String sql, @NotNull PageParams params);
}
