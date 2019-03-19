package com.aiolos.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "my_friends")
public class MyFriends {
    /**
     * 唯一id
     */
    @Id
    private String id;

    /**
     * 用户的id
     */
    @Column(name = "my_user_id")
    private String myUserId;

    /**
     * 用户B的id
     */
    @Column(name = "my_friend_user_id")
    private String myFriendUserId;

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
     * 获取用户的id
     *
     * @return my_user_id - 用户的id
     */
    public String getMyUserId() {
        return myUserId;
    }

    /**
     * 设置用户的id
     *
     * @param myUserId 用户的id
     */
    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    /**
     * 获取用户B的id
     *
     * @return my_friend_user_id - 用户B的id
     */
    public String getMyFriendUserId() {
        return myFriendUserId;
    }

    /**
     * 设置用户B的id
     *
     * @param myFriendUserId 用户B的id
     */
    public void setMyFriendUserId(String myFriendUserId) {
        this.myFriendUserId = myFriendUserId;
    }
}