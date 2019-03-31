package com.aiolos.enums;

import lombok.AllArgsConstructor;

/**
 * 消息签收状态
 * @author Aiolos
 * @date 2019-03-31 22:55
 */
@AllArgsConstructor
public enum MsgSignFlagEnum {

    UNSIGN(0, "未签收"),
    SIGNED(1, "已签收");

    public final Integer type;

    public final String content;

    public Integer getType() {
        return type;
    }
}
