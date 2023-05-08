package com.think.x.app.domain;

import com.think.x.core.base.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/2/24 11:20
 * @version: v1.0
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String username;

    private String mobile;

}
