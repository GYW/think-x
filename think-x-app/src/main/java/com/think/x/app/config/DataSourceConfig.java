package com.think.x.app.config;


import lombok.Data;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/2/22 17:06
 * @version: v1.0
 */
@Data
public class DataSourceConfig implements Serializable {
    private String driverClassName;

    private String jdbcUrl;

    private String username;

    private String password;

    private Integer maximumPoolSize;

    private Integer port;

    private String host;

    private String database;
}
