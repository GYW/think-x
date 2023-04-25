package com.think.x.network.server;

import com.think.x.network.INetworkServer;
import com.think.x.network.client.flux.TcpFluxClient;
import com.think.x.network.client.flux.VertxTcpFluxClient;
import com.think.x.network.constants.NetworkType;
import com.think.x.network.parser.flux.PayloadParser;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/21 19:44
 * @version: v1.0
 */
@Slf4j
public class VertxTcpServer implements INetworkServer {


    Collection<NetServer> tcpServers;

    private Supplier<PayloadParser> parserSupplier;

    @Setter
    private long keepAliveTimeout = Duration.ofMinutes(10).toMillis();

    @Getter
    private final String id;


    private final Sinks.Many<TcpFluxClient> processor = Sinks
            .many()
            .multicast()
            .onBackpressureBuffer(Integer.MAX_VALUE, false);

    @Getter
    @Setter
    private String lastError;

    @Getter
    @Setter
    private InetSocketAddress bind;

    public VertxTcpServer(String id) {
        this.id = id;
    }

    @Override
    public Flux<TcpFluxClient> handleConnection() {
        return processor.asFlux();
    }

    private void execute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("close tcp server error", e);
        }
    }

    @Override
    public InetSocketAddress getBindAddress() {
        return bind;
    }

    public void setParserSupplier(Supplier<PayloadParser> parserSupplier) {
        this.parserSupplier = parserSupplier;
    }

    public void setServer(Collection<NetServer> servers) {
        if (this.tcpServers != null && !this.tcpServers.isEmpty()) {
            shutdown();
        }
        this.tcpServers = servers;

        for (NetServer tcpServer : this.tcpServers) {
            tcpServer.connectHandler(this::acceptTcpConnection);
        }

    }

    protected void acceptTcpConnection(NetSocket socket) {
        if (processor.currentSubscriberCount() == 0) {
            log.warn("not handler for tcp client[{}]", socket.remoteAddress());
            socket.close();
            return;
        }
        VertxTcpFluxClient client = new VertxTcpFluxClient(id + "_" + socket.remoteAddress());
        client.setKeepAliveTimeoutMs(keepAliveTimeout);
        try {
            socket.exceptionHandler(err -> {
                log.error("tcp server client [{}] error", socket.remoteAddress(), err);
            });
            client.setRecordParser(parserSupplier.get());
            client.setSocket(socket);
            processor.emitNext(client, (signal, failure) -> failure == Sinks.EmitResult.FAIL_NON_SERIALIZED);
            log.debug("accept tcp client [{}] connection", socket.remoteAddress());
        } catch (Exception e) {
            log.error("create tcp server client error", e);
            client.shutdown();
        }
    }

    @Override
    public NetworkType getType() {
        return NetworkType.TCP_SERVER;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isAlive() {
        return tcpServers != null;
    }

}
