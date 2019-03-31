package com.aiolos.service.impl;

import com.aiolos.dao.*;
import com.aiolos.enums.MsgSignFlagEnum;
import com.aiolos.enums.SearchFriendsStatusEnum;
import com.aiolos.netty.ChatMsg;
import com.aiolos.pojo.FriendsRequest;
import com.aiolos.pojo.MyFriends;
import com.aiolos.pojo.Users;
import com.aiolos.pojo.vo.FriendRequestVO;
import com.aiolos.pojo.vo.MyFriendsVO;
import com.aiolos.service.IUserService;
import com.aiolos.utils.FastDFSClient;
import com.aiolos.utils.FileUtils;
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
import java.util.Date;
import java.util.List;

/**
 * @author Aiolos
 * @date 2019-03-19 16:56
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

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
            qrCodeUtils.createQRCode(qrCodePath, "QR:" + user.getUsername());
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
    protected Users queryUserById(String userId) {
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
        Example mfe = new Example(MyFriends.class);
        Criteria mfc = mfe.createCriteria();
        mfc.andEqualTo("myUserId", myUserId);
        mfc.andEqualTo("myFriendUserId", user.getId());
        MyFriends myFriendsRel = myFriendsMapper.selectOneByExample(mfe);

        if (myFriendsRel != null) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoByUsername(String username) {

        Example user = new Example(Users.class);
        Criteria uc = user.createCriteria();
        uc.andEqualTo("username", username);
        return usersMapper.selectOneByExample(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {

        Users friend = queryUserInfoByUsername(friendUsername);

        Example fre = new Example(FriendsRequest.class);
        Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId", myUserId);
        frc.andEqualTo("acceptUserId", friend.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(fre);

        // 没有好友请求记录
        if (friendsRequest == null) {

            String requestId = sid.nextShort();

            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            friendsRequestMapper.insert(request);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return usersMapperCustom.queryFriendRequestList(acceptUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delFriendRequest(String acceptUserId, String sendUserId) {

        Example fre = new Example(FriendsRequest.class);
        Criteria frc = fre.createCriteria();
        frc.andEqualTo("acceptUserId", acceptUserId);
        frc.andEqualTo("sendUserId", sendUserId);
        friendsRequestMapper.deleteByExample(fre);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void passFriendRequest(String acceptUserId, String sendUserId) {

        saveFriend(acceptUserId, sendUserId);
        saveFriend(sendUserId, acceptUserId);
        delFriendRequest(acceptUserId, sendUserId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {

        return usersMapperCustom.queryMyFriends(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsg chatMsg) {

        com.aiolos.pojo.ChatMsg chatDB = new com.aiolos.pojo.ChatMsg();
        String id = sid.nextShort();
        chatDB.setId(id);
        chatDB.setSendUserId(chatMsg.getSenderId());
        chatDB.setAcceptUserId(chatMsg.getReceiverId());
        chatDB.setCreateTime(new Date());
        chatDB.setSignFlag(MsgSignFlagEnum.UNSIGN.type);
        chatDB.setMsg(chatMsg.getMsg());
        chatMsgMapper.insert(chatDB);
        return id;
    }

    @Override
    public void updateMsgSigned(List<String> msgIdList) {

        usersMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void saveFriend(String acceptUserId, String sendUserId) {

        MyFriends myFriend = new MyFriends();
        String id = sid.nextShort();
        myFriend.setId(id);
        myFriend.setMyUserId(acceptUserId);
        myFriend.setMyFriendUserId(sendUserId);
        myFriendsMapper.insert(myFriend);
    }
}
