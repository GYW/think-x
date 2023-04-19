package com.think.x.core.base.params;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;


/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2020/10/19 15:12
 * @version: v1.0
 */
@Data
public class PageParams implements Serializable {

    private int size = 10;


    private int current = 1;
    /**
     * 排序字段,例:id,createdTime,updatedTime
     */
    private String sort;

    /**
     * 排序规则，descending 降序,ascending 升序
     */
    private String sortRule = "descending";

    /**
     * 查询参数
     */
    private Map<String, String> params = Collections.emptyMap();


}
