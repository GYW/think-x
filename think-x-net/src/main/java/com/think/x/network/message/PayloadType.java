package com.think.x.network.message;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 9:38
 * @version: v1.0
 */
public enum PayloadType {

  JSON, STRING, BINARY, HEX, UNKNOWN;

  public static PayloadType of(String of) {
    for (PayloadType value : PayloadType.values()) {
      if (value.name().equalsIgnoreCase(of)) {
        return value;
      }
    }
    return UNKNOWN;
  }
}
