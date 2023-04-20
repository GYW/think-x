package com.think.x.orm.repository;

import com.google.inject.Inject;
import com.think.x.core.base.domain.BaseEntity;
import com.think.x.core.base.params.PageData;
import com.think.x.core.base.params.PageParams;
import io.smallrye.mutiny.Uni;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.future.PromiseImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/7 15:20
 * @version: v1.0
 */
public abstract class MyRepository<T extends BaseEntity<E>, E extends Serializable> implements CrudRepository<T, E> {
    @Getter
    private final Class<T> target;

    public MyRepository(Class<T> target) {
        this.target = target;
    }

    @Inject
    @Setter(AccessLevel.PRIVATE)
    @Getter
    Mutiny.SessionFactory sessionFactory;

    public void setSessionFactory(@NotNull final Mutiny.SessionFactory sessionFactory) {
        Objects.requireNonNull(sessionFactory, "sessionFactory is null");
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Nullable
    public Future<T> execute(@NotNull BiFunction<Mutiny.Session, Mutiny.Transaction, Uni<T>> execution) {
        Promise<T> promise = new PromiseImpl<T>();
        sessionFactory.withTransaction((session, transaction) -> {
                    return execution.apply(session, transaction)
                            .call(session::flush);
                })
                .subscribe().with(promise::complete, promise::fail);
        return promise.future();
    }

    @Nullable
    @Override
    public Future<T> save(@NotNull T entity) {
        return execute((session, transaction) -> {
            return session.find(entity.getClass(), entity.getId())
                    .chain((t) -> {
                        if (Objects.isNull(t)) {
                            entity.setCreatedTime(LocalDateTime.now());
                            return session.persist(entity);
                        } else {
                            entity.setUpdatedTime(LocalDateTime.now());
                            return session.merge(entity);
                        }
                    }).map(p -> entity);
        });
    }

    @Nullable
    @Override
    public Future<Integer> update(@NotNull String sql, @NotNull Map<String, ?> params) {
        Promise<Integer> promise = new PromiseImpl<>();
        sessionFactory.withTransaction((session, transaction) -> {
            Mutiny.Query<Integer> query = session.createQuery(sql);
            params.forEach(query::setParameter);
            return query.executeUpdate().call(session::flush);
        }).subscribe().with(promise::complete, promise::fail);
        return promise.future();
    }

    @Nullable
    @Override
    public Future<Void> remove(@NotNull T entity) {
        Promise<Void> promise = new PromiseImpl<>();
        sessionFactory.withTransaction((session, transaction) -> {
                    return session.remove(entity).call(session::flush);
                }).subscribe()
                .with(promise::complete, promise::fail);
        return promise.future();
    }

    @Nullable
    @Override
    public Future<Boolean> batchSave(@NotNull Collection<T> entities) {
        return null;
    }

    @Nullable
    @Override
    public Future<Integer> delete(@Nullable E id) {
        Promise<Integer> promise = new PromiseImpl<>();
        sessionFactory.withTransaction((session, transaction) -> {
            Mutiny.Query<Integer> query = session.createQuery("delete from " + target.getName() + " where id =: id");
            query.setParameter("id", id);
            return query.executeUpdate().call(session::flush);
        }).subscribe().with(promise::complete, promise::fail);
        return promise.future();
    }

    @Nullable
    @Override
    public Future<Boolean> exists(@Nullable E id) {
        return null;
    }

    @Nullable
    @Override
    public Future<T> getOne(@Nullable E id) {
        return createQuery((session, transaction) -> session.find(target, id).map(entity -> entity));
    }

    @Nullable
    @Override
    public Future<List<T>> createQueryList(@NotNull String sql, @NotNull Map<String, ?> params) {
        Promise<List<T>> promise = new PromiseImpl<>();
        sessionFactory.withTransaction((session, transaction) -> {
                    Mutiny.Query<T> query = session.createQuery(sql, target);
                    params.forEach(query::setParameter);
                    return query.getResultList();
                })
                .subscribe()
                .with(t -> promise.complete(t), promise::fail);
        return promise.future();
    }

    @Nullable
    @Override
    public Future<T> createQueryOne(@NotNull String sql, @NotNull Map<String, ?> params) {
        return createQuery((session, transaction) -> {
            Mutiny.Query<T> query = session.createQuery(sql, target);
            params.forEach(query::setParameter);
            return query.getSingleResultOrNull();
        });
    }

    @Nullable
    @Override
    public Future<T> createQuery(BiFunction<Mutiny.Session, Mutiny.Transaction, Uni<T>> execution) {
        Promise<T> promise = new PromiseImpl<>();
        sessionFactory.withTransaction(execution)
                .subscribe()
                .with(promise::complete, promise::fail);
        return promise.future();
    }

    @Override
    public Future<Long> queryPageCount(@NotNull String sql, @NotNull PageParams params) {
        Promise<Long> promise = new PromiseImpl<>();
        StringBuilder countSql = new StringBuilder();
        if (sql.toLowerCase().startsWith("select")) {
            countSql.append("select count(1) from ").append("(").append(sql).append(")");
        } else {
            countSql.append("select count(1) ").append(sql);
        }
        sessionFactory.withSession((session) -> {
            Mutiny.Query<Long> query = session.createQuery(countSql.toString());
            params.getParams().forEach(query::setParameter);
            return query.getSingleResultOrNull();
        }).subscribe().with(promise::complete, promise::fail);
        return promise.future();

    }

    @Override
    public Future<List<T>> queryPageResult(@NotNull String sql, @NotNull PageParams params) {
        Promise<List<T>> promise = new PromiseImpl<>();
        sessionFactory.withSession(session -> {
            Mutiny.Query<T> query = session.createQuery(sql, target);
            params.getParams().forEach(query::setParameter);
            query.setFirstResult((params.getCurrent() - 1) * params.getSize()).setMaxResults(params.getSize());
            return query.getResultList();
        }).subscribe().with(promise::complete, promise::fail);
        return promise.future();
    }

    @Override
    public Future<PageData<T>> queryPage(@NotNull String sql, @NotNull PageParams params) {
        Promise<PageData<T>> promise = new PromiseImpl<>();
        PageData<T> pageData = new PageData<>(params.getCurrent(), params.getSize());
        CompositeFuture.all(this.queryPageCount(sql, params)
                                .onSuccess(pageData::setTotal)
                                .onFailure(promise::fail),
                        this.queryPageResult(sql, params)
                                .onSuccess(pageData::setRecords)
                                .onFailure(promise::fail))
                .onSuccess(it -> promise.complete(pageData))
                .onFailure(promise::fail);
        return promise.future();
    }

}
