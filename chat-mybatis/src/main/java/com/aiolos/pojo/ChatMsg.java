package com.aiolos.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "chat_msg")
public class ChatMsg {
    /**
     * 唯一id
     */
    @Id
    private String id;

    /**
     * 发送用户的id
     */
    @Column(name = "send_user_id")
    private String sendUserId;

    /**
     * 接收用户的id
     */
    @Column(name = "accept_user_id")
    private String acceptUserId;

    /**
     * 消息
     */
    private String msg;

    /**
     * 消息状态
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取唯一id
     *
     * @return id - 唯一id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置唯一id
     *
     * @param id 唯一id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取发送用户的id
     *
     * @return send_user_id - 发送用户的id
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置发送用户的id
     *
     * @param sendUserId 发送用户的id
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取接收用户的id
     *
     * @return accept_user_id - 接收用户的id
     */
    public String getAcceptUserId() {
        return acceptUserId;
    }

    /**
     * 设置接收用户的id
     *
     * @param acceptUserId 接收用户的id
     */
    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    /**
     * 获取消息
     *
     * @return msg - 消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取消息状态
     *
     * @return sign_flag - 消息状态
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置消息状态
     *
     * @param signFlag 消息状态
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}