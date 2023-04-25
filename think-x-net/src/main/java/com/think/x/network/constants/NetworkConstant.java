package com.think.x.network.constants;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/3/30 15:59
 * @version: v1.0
 */
public class NetworkConstant {

  public static final int SMALL_BUFFER_SIZE = Math.max(16,
    Integer.parseInt(System.getProperty("reactor.bufferSize.small", "256")));


}
