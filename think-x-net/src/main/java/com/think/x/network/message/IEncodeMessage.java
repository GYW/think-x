package com.think.x.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 14:21
 * @version: v1.0
 */
public interface IEncodeMessage {

  @NonNull
  ByteBuf getPayload();


  default String payloadAsString() {
    return getPayload().toString(StandardCharsets.UTF_8);
  }

  default byte[] payloadAsBytes() {
    return ByteBufUtil.getBytes(getPayload());
  }

  @Deprecated
  default byte[] getBytes() {
    return ByteBufUtil.getBytes(getPayload());
  }

  default byte[] getBytes(int offset, int len) {
    return ByteBufUtil.getBytes(getPayload(), offset, len);
  }

  @Deprecated
  default PayloadType getPayloadType() {
    return PayloadType.JSON;
  }

  static EmptyMessage empty() {
    return EmptyMessage.INSTANCE;
  }

  static IEncodeMessage simple(ByteBuf data) {
    return simple(data, PayloadType.BINARY);
  }

  static IEncodeMessage simple(ByteBuf data, PayloadType payloadType) {
    return SimpleEncodedMessage.of(data, payloadType);
  }
}
