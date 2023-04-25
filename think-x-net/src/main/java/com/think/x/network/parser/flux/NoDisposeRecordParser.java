package com.think.x.network.parser.flux;

import com.think.x.network.constants.NetworkConstant;
import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;


/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 不处理直接返回数据包
 * @author: GYW
 * @date: 2023/3/22 14:35
 * @version: v1.0
 */
@Slf4j
public class NoDisposeRecordParser implements PayloadParser {

    private final Sinks.Many<Buffer> sink = Sinks.many().multicast().onBackpressureBuffer(NetworkConstant.SMALL_BUFFER_SIZE, Boolean.FALSE);

    @Override
    public void handle(Buffer buffer) {
        sink.emitNext(buffer, (signal, failure) -> failure == Sinks.EmitResult.FAIL_NON_SERIALIZED);
    }

    @Override
    public Flux<Buffer> handlePayload() {
        return sink.asFlux();
    }

    @Override
    public void close() {
        sink.emitComplete((signal, failure) -> failure == Sinks.EmitResult.FAIL_NON_SERIALIZED);
    }
}
