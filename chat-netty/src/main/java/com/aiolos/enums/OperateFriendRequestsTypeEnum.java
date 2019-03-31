package com.aiolos.enums;

import lombok.AllArgsConstructor;

/**
 * @author Aiolos
 * @date 2019-03-30 10:36
 */
@AllArgsConstructor
public enum OperateFriendRequestsTypeEnum {

    IGNORE(0, "忽略"),
    PASS(1, "通过");

    public final Integer type;

    public final String msg;

    public Integer getType() {
        return type;
    }

    public static String getMsgByType(Integer type) {

        for (OperateFriendRequestsTypeEnum operType : OperateFriendRequestsTypeEnum.values()) {

            if (operType.getType() == type) {
                return operType.msg;
            }
        }
        return null;
    }
}
