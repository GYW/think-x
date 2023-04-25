package com.think.x.network.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/21 15:48
 * @version: v1.0
 */
public class ClassHolder {

  private static final Map<Class<?>, ClassDescription> CACHE = new ConcurrentHashMap<>();

  public static ClassDescription getDescription(Class<?> type) {
    return CACHE.computeIfAbsent(type, ClassDescription::new);
  }

}
