package com.think.x.network;


import com.think.x.network.client.flux.TcpFluxClient;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 网络服务
 * @author: GYW
 * @date: 2023/3/21 19:49
 * @version: v1.0
 */
public interface INetworkServer extends INetwork {

    InetSocketAddress getBindAddress();

    /**
     * 订阅客户端连接
     *
     * @return 客户端流
     * @see TcpFluxClient
     */
    Flux<TcpFluxClient> handleConnection();

}
