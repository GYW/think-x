package com.think.x.core.base.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/14 11:34
 * @version: v1.0
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
