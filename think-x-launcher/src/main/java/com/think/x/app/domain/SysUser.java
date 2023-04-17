package com.think.x.app.domain;

import com.think.x.core.base.domain.BaseEntity;

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
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String username;

    private String mobile;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
