package com.think.x.network.message;

import io.netty.buffer.ByteBuf;

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
public interface IDecodeMessage {
  ByteBuf getPayload();
}
