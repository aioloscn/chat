package com.aiolos.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "friends_request")
public class FriendsRequest {
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
     * 发送请求的时间
     */
    @Column(name = "request_date_time")
    private Date requestDateTime;

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
     * 获取发送请求的时间
     *
     * @return request_date_time - 发送请求的时间
     */
    public Date getRequestDateTime() {
        return requestDateTime;
    }

    /**
     * 设置发送请求的时间
     *
     * @param requestDateTime 发送请求的时间
     */
    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
}