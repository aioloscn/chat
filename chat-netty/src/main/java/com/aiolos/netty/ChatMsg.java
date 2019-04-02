package com.aiolos.netty;

import java.io.Serializable;

/**
 * 聊天模型对象
 * @author Aiolos
 * @date 2019-03-31 21:57
 */
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = -4919903198962693646L;

    private String senderId;   // 发送者的用户id

    private String receiverId; // 接收者的用户id

    private String msg; // 聊天内容

    private String msgId;   // 用于消息的签收

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
