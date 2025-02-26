package com.example.design.小傅哥拼团架构.多线程异步试算;

import lombok.Data;

@Data
public class RequestAttributes {
    /** 用户ID */
    private String userId;
    /** 商品ID */
    private String goodsId;
    /** 渠道 */
    private String source;
    /** 来源 */
    private String channel;
}
