package com.think.x.network.client.flux;

import com.think.x.network.INetwork;
import com.think.x.network.message.IEncodeMessage;
import com.think.x.network.message.TcpMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Duration;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 14:20
 * @version: v1.0
 */
public interface TcpFluxClient extends INetwork {

    /**
     * @return 客户端地址
     */
    InetSocketAddress address();

    /**
     * 发送消息给客户端
     *
     * @param message 消息
     * @return void
     */
    Mono<Void> sendMessage(IEncodeMessage message);

    /**
     * 接收来自客户端消息
     *
     * @return 消息流
     */
    Flux<IEncodeMessage> receiveMessage();

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 连接是否还存活
     */
    boolean isAlive();


    /**
     * 获取客户端远程地址
     *
     * @return 客户端远程地址
     */
    InetSocketAddress getRemoteAddress();

    /**
     * 订阅TCP消息,此消息是已经处理过粘拆包的完整消息
     *
     * @return TCP消息
     */
    Flux<TcpMessage> subscribe();

    /**
     * 向客户端发送数据
     *
     * @param message 数据对象
     * @return 发送结果
     */
    Mono<Boolean> send(TcpMessage message);

    void onDisconnect(Runnable disconnected);

    /**
     * 连接保活
     */
    void keepAlive();

    /**
     * 设置客户端心跳超时时间
     *
     * @param timeout 超时时间
     */
    void setKeepAliveTimeout(Duration timeout);

    /**
     * 重置
     */
    void reset();
}
