package com.think.x.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 14:30
 * @version: v1.0
 */
@Getter
public class SimpleEncodedMessage implements IEncodeMessage {
  public SimpleEncodedMessage(ByteBuf payload, PayloadType payloadType) {
    this.payload = payload;
    this.payloadType = payloadType;
  }

  private final ByteBuf payload;

  private final PayloadType payloadType;

  public static SimpleEncodedMessage of(ByteBuf byteBuf, PayloadType payloadType) {
    return new SimpleEncodedMessage(byteBuf, payloadType);
  }

  @Override
  public String toString() {

    StringBuilder builder = new StringBuilder();

    if (ByteBufUtil.isText(payload, StandardCharsets.UTF_8)) {
      builder.append(payload.toString(StandardCharsets.UTF_8));
    } else {
      ByteBufUtil.appendPrettyHexDump(builder, payload);
    }
    return builder.toString();
  }
}
