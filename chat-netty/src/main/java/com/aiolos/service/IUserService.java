package com.aiolos.service;

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
    public boolean queryUsernameIsExist(String username);
}
