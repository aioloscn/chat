package com.aiolos.service.impl;

import com.aiolos.dao.MyFriendsMapper;
import com.aiolos.dao.UsersMapper;
import com.aiolos.enums.SearchFriendsStatusEnum;
import com.aiolos.pojo.Users;
import com.aiolos.service.IUserService;
import com.aiolos.utils.FastDFSClient;
import com.aiolos.utils.FileUtils;
import com.aiolos.utils.MD5Utils;
import com.aiolos.utils.QRCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;

/**
 * @author Aiolos
 * @date 2019-03-19 16:56
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Users user = new Users();
        user.setUsername(username);

        Users result = usersMapper.selectOne(user);
        return result != null ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String pwd) {

        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", pwd);

        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {

        String userId = sid.nextShort();

        // 为每个用户生成一个唯一的二维码
        String qrCodePath = "/developer/qrcode/user" + userId + "qrcode.png";
        try {
            qrCodeUtils.createQRCode(qrCodePath, MD5Utils.getMD5Str(user.getUsername()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = StringUtils.EMPTY;
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.setQrcode(qrCodeUrl);
        user.setId(userId);
        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users users) {

        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserById(users.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditonSearchFriends(String myUserId, String friendUsername) {

        Users user = queryUserInfoByUsername(friendUsername);

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        if (user == null)
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;

        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        if (user.getId().equals(myUserId))
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;

        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]

        return null;
    }

    public Users queryUserInfoByUsername(String username) {

        Example user = new Example(Users.class);
        Criteria uc = user.createCriteria();
        uc.andEqualTo("username", username);
        return usersMapper.selectOneByExample(user);
    }
}
