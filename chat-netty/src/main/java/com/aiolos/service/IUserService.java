package com.aiolos.service;

import com.aiolos.pojo.Users;

/**
 * @author Aiolos
 * @date 2019-03-19 16:55
 */
public interface IUserService {

    /**
     * 判断用户名是否存在
     * @param username  用户名
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 查询用户是否存在
     * @param username  用户名
     * @param pwd   密码
     * @return
     */
    Users queryUserForLogin(String username, String pwd);

    /**
     * 注册
     * @param user
     * @return
     */
    Users saveUser(Users user);

    /**
     * 修改用户记录
     * @param users
     */
    Users updateUserInfo(Users users);

    /**
     * 前置条件搜索朋友
     * @param myUserId 我的ID
     * @param friendUsername 搜索的用户名
     * @return
     */
    Integer preconditonSearchFriends(String myUserId, String friendUsername);

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    Users queryUserInfoByUsername(String username);

    /**
     * 添加好友
     * @param myUserId
     * @param friendUsername
     */
    void sendFriendRequest(String myUserId, String friendUsername);
}
