package com.think.x.app.config;


import java.io.Serializable;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/2/22 16:11
 * @version: v1.0
 */
public class HttpServerConfig implements Serializable {

    private Integer port;

    private String contextPath;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
