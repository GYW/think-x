package com.think.x.network.constants;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/21 16:13
 * @version: v1.0
 */
public class NetworkTypes {

    private static final Map<String, NetworkType> all = new ConcurrentHashMap<>();

    public static void register(Collection<NetworkType> transport) {
        transport.forEach(NetworkTypes::register);
    }

    public static void register(NetworkType transport) {
        all.put(transport.getId().toUpperCase(), transport);
    }

    public static List<NetworkType> get() {
        return new ArrayList<>(all.values());
    }

    public static Optional<NetworkType> lookup(String id) {
        return Optional.ofNullable(all.get(id.toUpperCase()));
    }

}
