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
}
