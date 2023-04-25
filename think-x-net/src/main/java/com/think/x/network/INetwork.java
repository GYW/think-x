package com.think.x.network;


import com.think.x.network.constants.NetworkType;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description: 网络服务基础接口
 * @author: GYW
 * @date: 2023/3/21 19:44
 * @version: v1.0
 */
public interface INetwork {


  String getId();

  /**
   * 网络类型
   *
   * @return
   */
  NetworkType getType();

  /**
   *
   */
  void shutdown();

  /**
   * 存活状态
   *
   * @return
   */
  boolean isAlive();

}
