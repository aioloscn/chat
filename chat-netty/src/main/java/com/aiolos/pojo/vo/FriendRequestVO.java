package com.aiolos.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 好友请求发送方的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestVO {

    private String sendUserId;

    /**
     * 请求者的用户名
     */
    private String sendUsername;

    /**
     * 请求者的头像
     */
    private String sendFaceImage;

    /**
     * 请求者的昵称
     */
    private String sendNickname;

}