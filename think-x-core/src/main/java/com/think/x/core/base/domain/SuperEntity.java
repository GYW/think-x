package com.think.x.core.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.think.x.core.constants.DateConstants;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 实体超类
 * @author: GYW
 * @date: 2023/04/07 12:49
 * @version: v1.0
 */
@MappedSuperclass
public class SuperEntity<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIELD_ID = "id";
    public static final String CREATE_TIME_COLUMN = "created_time";
    public static final String CREATE_USER_COLUMN = "created_by";

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowFlakeIdWorker", strategy = "com.think.x.core.base.id.SnowFlakeIdWorker")
    @GeneratedValue(generator = "snowFlakeIdWorker")
    @Column(name = FIELD_ID)
    protected T id;

    /**
     * 创建时间
     */
    @Column(name = CREATE_TIME_COLUMN)
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    protected LocalDateTime createdTime;

    /**
     * 创建人ID
     */
    @Column(name = CREATE_USER_COLUMN)
    protected T createdBy;

    public SuperEntity(@NotNull T id, LocalDateTime createdTime, T createdBy) {
        this.id = id;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
    }

    public SuperEntity() {
    }

    public T getId() {
        return id;
    }

    public void setId(@NotNull T id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public T getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(T createdBy) {
        this.createdBy = createdBy;
    }
}
