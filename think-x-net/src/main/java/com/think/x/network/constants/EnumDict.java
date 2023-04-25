package com.think.x.network.constants;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed By GYW
 * 枚举字典,使用枚举来实现数据字典,可通过集成此接口来实现一些功能.
 * ️:如果使用了位运算来判断枚举,枚举数量不要超过64个,且顺序不要随意变动!
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/10 15:59
 * @version: v1.0
 */
public interface EnumDict<V> {


    /**
     * 枚举选项的值,通常由字母或者数字组成,并且在同一个枚举中值唯一;对应数据库中的值通常也为此值
     *
     * @return 枚举的值
     */
    V getValue();

    /**
     * 枚举字典选项的文本,通常为中文
     *
     * @return 枚举的文本
     */
    String getText();

    /**
     * {@link Enum#ordinal()}
     *
     * @return 枚举序号, 如果枚举顺序改变, 此值将被变动
     */
    int order();

    default long index() {
        return order();
    }

    default long getMask() {
        return 1L << index();
    }

    /**
     * 对比是否和value相等,对比地址,值,value转为string忽略大小写对比,text忽略大小写对比
     *
     * @param v value
     * @return 是否相等
     */
    @SuppressWarnings("all")
    default boolean eq(Object v) {
        if (v == null) {
            return false;
        }
        if (v instanceof Object[]) {
            v = Arrays.asList(v);
        }
        if (v instanceof Collection) {
            return ((Collection) v).stream().anyMatch(this::eq);
        }
        if (v instanceof Map) {
            v = ((Map) v).getOrDefault("value", ((Map) v).get("text"));
        }
        return this == v
                || getValue() == v
                || getValue().equals(v)
                || String.valueOf(getValue()).equalsIgnoreCase(String.valueOf(v))
                || getText().equalsIgnoreCase(String.valueOf(v)
        );
    }

    default boolean in(long mask) {
        return (mask & getMask()) != 0;
    }

    default boolean in(EnumDict<V>... dict) {
        return in(toMask(dict));
    }

    /**
     * 枚举选项的描述,对一个选项进行详细的描述有时候是必要的.默认值为{@link EnumDict#getText()}
     *
     * @return 描述
     */
    default String getComments() {
        return getText();
    }


    /**
     * 从指定的枚举类中查找想要的枚举,并返回一个{@link Optional},如果未找到,则返回一个{@link Optional#empty()}
     *
     * @param type      实现了{@link EnumDict}的枚举类
     * @param predicate 判断逻辑
     * @param <T>       枚举类型
     * @return 查找到的结果
     */
    @SuppressWarnings("all")
    static <T extends Enum & EnumDict> Optional<T> find(Class<T> type, Predicate<T> predicate) {
        ClassDescription description = ClassHolder.getDescription(type);
        if (description.isEnumType()) {
            for (Object enumDict : description.getEnums()) {
                if (predicate.test((T) enumDict)) {
                    return Optional.of((T) enumDict);
                }
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("all")
    static <T extends Enum & EnumDict> List<T> findList(Class<T> type, Predicate<T> predicate) {
        ClassDescription description = ClassHolder.getDescription(type);
        if (description.isEnumType()) {
            return Arrays.stream(description.getEnums())
                    .map(v -> (T) v)
                    .filter(predicate)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 根据枚举的{@link EnumDict#getValue()}来查找.
     *
     * @see EnumDict#find(Class, Predicate)
     */
    static <T extends Enum & EnumDict<?>> Optional<T> findByValue(Class<T> type, Object value) {
        return find(type, e -> e.getValue() == value || e.getValue().equals(value) || String
                .valueOf(e.getValue())
                .equalsIgnoreCase(String.valueOf(value)));
    }

    /**
     * 根据枚举的{@link EnumDict#getText()} 来查找.
     *
     * @see EnumDict#find(Class, Predicate)
     */
    static <T extends Enum & EnumDict> Optional<T> findByText(Class<T> type, String text) {
        return find(type, e -> e.getText().equalsIgnoreCase(text));
    }

    /**
     * 根据枚举的{@link EnumDict#getValue()},{@link EnumDict#getText()}来查找.
     *
     * @see EnumDict#find(Class, Predicate)
     */
    static <T extends Enum & EnumDict> Optional<T> find(Class<T> type, Object target) {
        return find(type, v -> v.eq(target));
    }

    @SafeVarargs
    static <T extends EnumDict> long toMask(T... t) {
        if (t == null) {
            return 0L;
        }
        long value = 0L;
        for (T t1 : t) {
            value |= t1.getMask();
        }
        return value;
    }


    @SafeVarargs
    static <T extends EnumDict> boolean maskIn(long mask, T... t) {
        long value = toMask(t);
        return (mask & value) == value;
    }

    @SafeVarargs
    static <T extends EnumDict> boolean maskInAny(long mask, T... t) {
        long value = toMask(t);
        return (mask & value) != 0;
    }

    static <T extends EnumDict> List<T> getByMask(List<T> allOptions, long mask) {
        if (allOptions.size() >= 64) {
            throw new UnsupportedOperationException("不支持选项超过64个数据字典!");
        }
        List<T> arr = new ArrayList<>();
        List<T> all = allOptions;
        for (T t : all) {
            if (t.in(mask)) {
                arr.add(t);
            }
        }
        return arr;
    }

    static <T extends EnumDict> List<T> getByMask(Supplier<List<T>> allOptionsSupplier, long mask) {
        return getByMask(allOptionsSupplier.get(), mask);
    }


    static <T extends Enum & EnumDict> List<T> getByMask(Class<T> tClass, long mask) {

        return getByMask(Arrays.asList(tClass.getEnumConstants()), mask);
    }

}
