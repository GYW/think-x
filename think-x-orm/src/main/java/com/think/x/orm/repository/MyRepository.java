package com.think.x.orm.repository;

import com.google.inject.Inject;
import com.think.x.core.base.domain.BaseEntity;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.future.PromiseImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    @Override
    public <T> Future withTransaction(@NotNull BiFunction<Mutiny.Session, Mutiny.Transaction, Uni<T>> execution) {
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
        return withTransaction((session, transaction) -> {
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
    public Future<Boolean> remove(@NotNull T entity) {
        return null;
    }

    @Nullable
    @Override
    public Future<Boolean> batchSave(@NotNull Collection<T> entities) {
        return null;
    }

    @Nullable
    @Override
    public Future<Boolean> delete(@Nullable E id) {
        return null;
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
    public Future<List<T>> createQueryList(@NotNull String sql,@NotNull Map<String, ?> params) {
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
    public Future<Integer> execute(@NotNull String sql, @NotNull Map<String, ?> params) {
        return null;
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
}
