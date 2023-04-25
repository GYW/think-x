package com.think.x.network.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 待优化，增加查询方法，使用系统缓存ConcurrentHashMap进行优化
 * @author: GYW
 * @date: 2023/3/21 15:56
 * @version: v1.0
 */
public enum NetworkType implements EnumDict<String> {

  TCP_CLIENT("TCP客户端"),
  TCP_SERVER("TCP服务"),

  MQTT_CLIENT("MQTT客户端"),
  MQTT_SERVER("MQTT服务"),

  WEB_SOCKET_CLIENT("WebSocket客户端"),
  WEB_SOCKET_SERVER("WebSocket服务"),

  UDP("UDP"),

  ;

  private String name;

  static {
    NetworkTypes.register(Arrays.asList(NetworkType.values()));
  }

  NetworkType(String name) {
    this.name = name;
  }


  public String getId() {
    return name();
  }


  public String getText() {
    return name;
  }


  public String getValue() {
    return name();
  }

  @Override
  public int order() {
    return 0;
  }

  /**
   * 获取所有支持的网络组件类型
   *
   * @return 所有支持的网络组件类型
   */
  static List<NetworkType> getAll() {
    return NetworkTypes.get();
  }

  /**
   * 根据网络组件类型ID获取类型对象
   *
   * @param id ID
   * @return Optional
   */
  static Optional<NetworkType> lookup(String id) {
    return NetworkTypes.lookup(id);
  }

}
