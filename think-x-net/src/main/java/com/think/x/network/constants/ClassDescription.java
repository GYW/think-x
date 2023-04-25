package com.think.x.network.constants;

import lombok.Getter;

import java.util.Collection;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/21 15:47
 * @version: v1.0
 */
@Getter
public class ClassDescription {
  private final Class<?> type;

  private final boolean collectionType;
  private final boolean arrayType;
  private final boolean enumType;
  private final boolean enumDict;
  private final int fieldSize;

  private final Object[] enums;

  public ClassDescription(Class<?> type) {
    this.type = type;
    collectionType = Collection.class.isAssignableFrom(type);
    enumDict = EnumDict.class.isAssignableFrom(type);
    arrayType = type.isArray();
    enumType = type.isEnum();
    fieldSize = type.getDeclaredFields().length;
    if (enumType) {
      enums = type.getEnumConstants();
    } else {
      enums = null;
    }
  }
}
