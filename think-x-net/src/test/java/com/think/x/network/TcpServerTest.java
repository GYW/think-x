package com.think.x.network;

import com.think.x.network.parser.flux.NoDisposeRecordParser;
import com.think.x.network.server.VertxTcpServer;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/24 18:02
 * @version: v1.0
 */
//@Slf4j
@ExtendWith(VertxExtension.class)
public class TcpServerTest {
    Vertx vertx = Vertx.vertx();

    public final static String IP = "127.0.0.1";
    public final static Integer PORT = 6666;

    @Test
    void start_server() {
        VertxTcpServer tcpServer = new VertxTcpServer("TCP-SERVER");
//        int numberOfInstance = Math.max(1, 2);
        List<NetServer> instances = new ArrayList<>(2);
        Flux.range(1, 2).toStream().forEach(i -> instances.add(vertx.createNetServer()));
        tcpServer.setParserSupplier(NoDisposeRecordParser::new);
        tcpServer.setServer(instances);
        tcpServer.setKeepAliveTimeout(1000L);
        tcpServer.setBind(new InetSocketAddress(IP, PORT));
        instances.forEach(netServer -> {
            vertx.nettyEventLoopGroup().execute(() -> {
                netServer.connectHandler(socket -> {
                    socket.handler(buffer -> {
//                        log.info(buffer.toString());
                        System.out.println(buffer.toString());
                        socket.write(netServer.actualPort() + ",response: " + buffer.toString(), "UTF-8");
                    });
                    socket.closeHandler((res) -> {
//                        log.error("socked closed,{}", res);
                        System.out.println("socked closed,{}" + res);
                    });
                });
                netServer.listen(PORT, IP).onSuccess((r) -> {
                    System.out.println("tcp server startup on {}" + r.actualPort());
//                    log.info("tcp server startup on {}", r.actualPort());
                }).onFailure((e) -> {
                    System.out.println("startup tcp server error,{}" + e.getCause().getMessage());
//                    log.error("startup tcp server error,{}", e.getCause().getMessage());
                });

            });
        });
    }
}
