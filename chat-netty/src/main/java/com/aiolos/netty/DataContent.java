package com.aiolos.netty;

import java.io.Serializable;

/**
 * @author Aiolos
 * @date 2019-03-31 21:50
 */
public class DataContent implements Serializable {

    private static final long serialVersionUID = 1914283656209650830L;

    private Integer action; // 动作类型

    private ChatMsg chatMsg;    // 用户的聊天内容entity

    private String extend;  // 扩展字段

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public ChatMsg getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
