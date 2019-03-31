package com.aiolos.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyFriendsVO {

    private String friendUserId;

    /**
     * 用户名，账号
     */
    private String friendUsername;

    /**
     * 头像
     */
    private String friendFaceImage;

    /**
     * 昵称
     */
    private String friendNickname;

}