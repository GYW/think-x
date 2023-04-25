package com.think.x.network.client.flux;

import com.think.x.network.constants.NetworkConstant;
import com.think.x.network.constants.NetworkType;
import com.think.x.network.message.IEncodeMessage;
import com.think.x.network.message.TcpMessage;
import com.think.x.network.parser.flux.PayloadParser;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/30 15:45
 * @version: v1.0
 */
@Slf4j
public class VertxTcpFluxClient implements TcpFluxClient {

    public volatile NetClient client;

    public NetSocket socket;

    volatile PayloadParser payloadParser;

    @Getter
    private final String id;

    @Setter
    private long keepAliveTimeoutMs = Duration.ofMinutes(10).toMillis();

    private volatile long lastKeepAliveTime = System.currentTimeMillis();

    private final List<Runnable> disconnectListener = new CopyOnWriteArrayList<>();
    //  Sinks.many().multicast() 如果没有订阅者，那么接收的消息直接丢弃
    //  Sinks.many().unicast() 如果没有订阅者，那么保存接收的消息直到第一个订阅者订阅
    // Sinks.many().replay() 不管有多少订阅者，都保存所有消息
    private final Sinks.Many<TcpMessage> sinks = Sinks.many().multicast().onBackpressureBuffer(NetworkConstant.SMALL_BUFFER_SIZE, Boolean.FALSE);

    private final boolean serverClient;


    public VertxTcpFluxClient(String id) {
        this.id = id;
        this.serverClient = true;
    }

    @Override
    public void keepAlive() {
        lastKeepAliveTime = System.currentTimeMillis();
    }

    @Override
    public void setKeepAliveTimeout(Duration timeout) {
        keepAliveTimeoutMs = timeout.toMillis();
    }

    @Override
    public void reset() {
        if (null != payloadParser) {
            payloadParser.reset();
        }
    }

    @Override
    public InetSocketAddress address() {
        return getRemoteAddress();
    }

    @Override
    public Mono<Void> sendMessage(IEncodeMessage message) {
        return Mono.create((sink) -> {
            if (socket == null) {
                sink.error(new SocketException("socket closed"));
                log.error("socket closed");
                return;
            }
            ByteBuf buf = message.getPayload();
            Buffer buffer = Buffer.buffer(buf);
            socket.write(buffer).onComplete((r) -> ReferenceCountUtil.safeRelease(buf))
                    .onSuccess((r) -> {
                        keepAlive();
                        sink.success();
                        log.info("socket send success");
                    }).onFailure((e) -> {
                        sink.error(e);
                        log.error("socket fail,{}", e.getCause().getMessage());
                    });
        });
    }

    @Override
    public Flux<IEncodeMessage> receiveMessage() {
        return this
                .subscribe()
                .cast(IEncodeMessage.class);
    }

    @Override
    public void disconnect() {
        shutdown();
    }

    @Override
    public boolean isAlive() {
        return socket != null && (keepAliveTimeoutMs < 0 || System.currentTimeMillis() - lastKeepAliveTime < keepAliveTimeoutMs);
    }


    protected void received(TcpMessage message) {
        sinks.emitNext(message, (signalType, emitResult) -> emitResult == Sinks.EmitResult.FAIL_NON_SERIALIZED);
    }

    @Override
    public Flux<TcpMessage> subscribe() {
        return sinks.asFlux();
    }

    private void execute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("close tcp client error", e);
        }
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        if (null == socket) {
            return null;
        }
        SocketAddress socketAddress = socket.remoteAddress();
        return InetSocketAddress.createUnresolved(socketAddress.host(), socketAddress.port());
    }

    @Override
    public NetworkType getType() {
        return NetworkType.TCP_CLIENT;
    }

    @Override
    public void shutdown() {
        if (socket == null) {
            return;
        }
        log.info("tcp client [{}] disconnect", getId());
        synchronized (this) {
            if (null != client) {
                execute(client::close);
                client = null;
            }
            if (null != socket) {
                execute(socket::close);
                this.socket = null;
            }
            if (null != payloadParser) {
                execute(payloadParser::close);
                payloadParser = null;
            }
        }
        for (Runnable runnable : disconnectListener) {
            execute(runnable);
        }
        disconnectListener.clear();
        if (serverClient) {
            sinks.tryEmitComplete();
        }
    }

    public void setClient(NetClient client) {
        if (this.client != null && this.client != client) {
            this.client.close();
        }
        keepAlive();
        this.client = client;
    }

    public void setRecordParser(PayloadParser payloadParser) {
        synchronized (this) {
            if (null != this.payloadParser && this.payloadParser != payloadParser) {
                this.payloadParser.close();
            }
            this.payloadParser = payloadParser;
            this.payloadParser
                    .handlePayload()
                    .subscribe(buffer -> received(new TcpMessage(buffer.getByteBuf())));
        }
    }

    public void setSocket(NetSocket socket) {
        synchronized (this) {
            Objects.requireNonNull(payloadParser);
            if (this.socket != null && this.socket != socket) {
                this.socket.close();
            }

            this.socket = socket
                    .closeHandler(v -> shutdown())
                    .handler(buffer -> {
                        if (log.isDebugEnabled()) {
                            log.info("handle tcp client[{}] payload:[{}]",
                                    socket.remoteAddress(),
                                    buffer.toString());
//            Hex.encodeHexString(buffer.getBytes()));
                        }
                        keepAlive();
                        payloadParser.handle(buffer);
                        if (this.socket != socket) {
                            log.warn("tcp client [{}] memory leak ", socket.remoteAddress());
                            socket.close();
                        }
                    });
        }
    }

    @Override
    public Mono<Boolean> send(TcpMessage message) {
        return sendMessage(message).thenReturn(Boolean.TRUE);

    }

    @Override
    public void onDisconnect(Runnable disconnected) {
        disconnectListener.add(disconnected);
    }
}
