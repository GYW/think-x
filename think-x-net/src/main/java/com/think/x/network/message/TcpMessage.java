package com.think.x.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 14:25
 * @version: v1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcpMessage implements IEncodeMessage {

  private ByteBuf payload;


  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    if (ByteBufUtil.isText(payload, StandardCharsets.UTF_8)) {
      builder.append(payloadAsString());
    } else {
      ByteBufUtil.appendPrettyHexDump(builder, payload);
    }

    return builder.toString();
  }
}
