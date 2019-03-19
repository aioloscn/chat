package com.aiolos.service.impl;

import com.aiolos.dao.UsersMapper;
import com.aiolos.pojo.Users;
import com.aiolos.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aiolos
 * @date 2019-03-19 16:56
 */
@Service
public class UserServiceImpl implements IUserService {


    @Override
    public boolean queryUsernameIsExist(String username) {

        Users user = new Users();
        return false;
    }
}
