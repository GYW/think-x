package com.think.x.core.base.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.think.x.core.constants.DateConstants;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 实体基类
 * @author: GYW
 * @date: 2023/3/31 16:02
 * @version: v1.0
 */
@MappedSuperclass
public class BaseEntity<T extends Serializable> extends SuperEntity<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 最后修改时间
     */
//    @Audited
    @Column(name = "updated_time")
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    protected LocalDateTime updatedTime;

    /**
     * 最后修改人ID
     */
//    @Audited
    @Column(name = "updated_by")
    protected T updatedBy;

    public BaseEntity(T id, LocalDateTime createdTime, T createBy, LocalDateTime updatedTime, T updatedBy) {
        super(id, createdTime, createBy);
        this.updatedTime = updatedTime;
        this.updatedBy = updatedBy;
    }

    public BaseEntity() {
        super();
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public T getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(T updatedBy) {
        this.updatedBy = updatedBy;
    }
}
