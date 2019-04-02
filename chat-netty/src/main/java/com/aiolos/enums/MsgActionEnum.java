package com.aiolos.enums;

import lombok.AllArgsConstructor;

/**
 * 发送消息的动作
 * @author Aiolos
 * @date 2019-03-31 22:04
 */
@AllArgsConstructor
public enum MsgActionEnum {

    CONNECT(1, "第一次(或重连)初始化连接"),
    CHAT(2, "聊天消息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "客户端保持心跳"),
    PULL_FRIEND(5, "重新拉取好友");

    public final Integer type;

    public final String content;

    public Integer getType() {
        return type;
    }

}
