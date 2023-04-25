package com.think.x.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NonNull;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 14:28
 * @version: v1.0
 */
public final class EmptyMessage implements IEncodeMessage {

    public static final EmptyMessage INSTANCE = new EmptyMessage();

    private EmptyMessage() {
    }

    @NonNull
    @Override
    public ByteBuf getPayload() {
        return Unpooled.wrappedBuffer(new byte[0]);
    }

    @Override
    public String toString() {
        return "empty message";
    }
}
