package com.think.x.core.base.domain;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 多租户实体基类
 * @author: GYW
 * @date: 2023/04/07 12:49
 * @version: v1.0
 */

public class TenantEntity<M, T extends Serializable> extends BaseEntity<T> {
    private static final String TENANT_ID = "tenant_id";
    @Column(name = TENANT_ID)
    private M tenantId;

    public TenantEntity() {
        super();
    }

    public TenantEntity(T id, LocalDateTime createdTime, T createBy, LocalDateTime updatedTime, T updatedBy, M tenantId) {
        super(id, createdTime, createBy, updatedTime, updatedBy);
        this.tenantId = tenantId;
    }
}
