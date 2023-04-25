package com.think.x.network.parser.flux;


import io.vertx.core.buffer.Buffer;
import reactor.core.publisher.Flux;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/22 14:35
 * @version: v1.0
 */
public interface PayloadParser {

  /**
   * 处理一个数据包
   *
   * @param buffer 数据包
   */
  void handle(Buffer buffer);

  /**
   * 订阅完整的数据包流,每一个元素为一个完整的数据包
   *
   * @return 完整数据包流
   */
  Flux<Buffer> handlePayload();

  /**
   * 关闭以释放相关资源
   */
  void close();

  /**
   * 重置规则
   */
  default void reset(){}
}
