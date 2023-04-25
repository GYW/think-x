package com.think.x.network;

import com.think.x.network.client.flux.VertxTcpFluxClient;
import com.think.x.network.constants.NetworkType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/24 10:50
 * @version: v1.0
 */
@Slf4j
public class NetworkHolder {
    private static final NetworkHolder INSTANCE = new NetworkHolder();

    public static NetworkHolder getInstance() {
        return INSTANCE;
    }

    /**
     * 网络缓存
     */
    private final Map<NetworkType, Map<String, VertxTcpFluxClient>> vertxTcpClients = new ConcurrentHashMap<>();

    public Mono<VertxTcpFluxClient> getNetwork(NetworkType type, String id) {
        Map<String, VertxTcpFluxClient> networkMap = getNetworkMap(type);
        return Mono.just(networkMap.get(id));
    }

    public Map<String, VertxTcpFluxClient> getNetworkMap(NetworkType type) {
        return vertxTcpClients.get(type);
    }

    public void addNetwork(NetworkType type, String networkId, VertxTcpFluxClient t) {
        vertxTcpClients.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(networkId, t);
    }

    public void removeNetwork(NetworkType type, String networkId) {
        Map<String, VertxTcpFluxClient> networkMap = getNetworkMap(type);
        networkMap.remove(networkId);
    }

    public void clearNetwork(NetworkType type) {
        vertxTcpClients.remove(type);
    }

    public void clearAllNetwork() {
        vertxTcpClients.clear();
    }

    public Map<NetworkType, Map<String, VertxTcpFluxClient>> getVertxTcpClients() {
        return vertxTcpClients;
    }

}
