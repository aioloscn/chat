package com.aiolos.controller;

import com.aiolos.enums.OperateFriendRequestsTypeEnum;
import com.aiolos.enums.SearchFriendsStatusEnum;
import com.aiolos.pojo.Users;
import com.aiolos.pojo.bo.UsersBO;
import com.aiolos.pojo.vo.MyFriendsVO;
import com.aiolos.pojo.vo.UsersVO;
import com.aiolos.service.IUserService;
import com.aiolos.utils.ChatJSONResult;
import com.aiolos.utils.FastDFSClient;
import com.aiolos.utils.FileUtils;
import com.aiolos.utils.MD5Utils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Aiolos
 * @date 2019-03-19 16:53
 */
@RestController
@RequestMapping("/u")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @PostMapping(value = "/registOrLogin")
    public ChatJSONResult registOrLogin(Users user) throws Exception {

        log.info("request -> {}, user -> {}", "registOrLogin", JSON.toJSONString(user));

        // 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return ChatJSONResult.errorMsg("用户名和密码不能为空...");
        }

        // 判断用户名是否存在，如果在就登录，如果不在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        Users userResult = null;
        if (usernameIsExist) {
            // 登录
            userResult = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return ChatJSONResult.errorMsg("用户名或密码不正确...");
            }
        } else {
            // 注册
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            userResult = userService.saveUser(user);
        }

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);

        return ChatJSONResult.ok(usersVO);
    }

    @PostMapping("/uploadFaceBase64")
    public ChatJSONResult uploadFaceBase64(UsersBO usersBO) throws Exception {

        log.info("request -> {}, usersBO -> {}", "uploadFaceBase64", JSON.toJSONString(usersBO));

        // 获取前段传过来的base64字符串，然后转换为文件对象再上传
        String base64Data = usersBO.getFaceData();
        String userFacePath = "/developer/portrait/" + usersBO.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);

        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);

        // 获取缩略图的url
        String thumb = "_80x80.";
        String arr[] = url.split("\\.");
        String thumbImgUrl = arr[0] + thumb + arr[1];

        // 更新用户头像
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setFaceImage(thumbImgUrl);
        user.setFaceImageBig(url);

        Users userResult = userService.updateUserInfo(user);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);

        return ChatJSONResult.ok(usersVO);
    }

    @PostMapping("/setNickname")
    public ChatJSONResult setNickname(UsersBO usersBO) throws Exception {

        log.info("request -> {}, usersBO -> {}", "setNickname", JSON.toJSONString(usersBO));

        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setNickname(usersBO.getNickname());

        Users userResult = userService.updateUserInfo(user);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);

        return ChatJSONResult.ok(usersVO);
    }

    /**
     * 搜索好友接口，根据用户名匹配查询
     * @param myUserId
     * @param friendUsername
     * @return
     */
    @PostMapping("/search")
    public ChatJSONResult searchUser(String myUserId, String friendUsername) {

        log.info("request -> {}, myUserId -> {}, friendUsername -> {}", "search", myUserId, friendUsername);

        if  (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername))
            return ChatJSONResult.errorMsg("");

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditonSearchFriends(myUserId, friendUsername);

        if (status == SearchFriendsStatusEnum.SUCCESS.status) {

            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO userVO = new UsersVO();
            BeanUtils.copyProperties(user, userVO);
            return ChatJSONResult.ok(userVO);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return ChatJSONResult.errorMsg(errorMsg);
        }
    }

    /**
     * 添加好友
     * @param myUserId
     * @param friendUsername
     * @return
     */
    @PostMapping("/addFriendRequest")
    public ChatJSONResult addFriendRequest(String myUserId, String friendUsername) {

        log.info("request -> {}, myUserId -> {}, friendUsername -> {}", "addFriendRequest", myUserId, friendUsername);

        if  (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername))
            return ChatJSONResult.errorMsg("");

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditonSearchFriends(myUserId, friendUsername);

        if (status == SearchFriendsStatusEnum.SUCCESS.status) {

            userService.sendFriendRequest(myUserId, friendUsername);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return ChatJSONResult.errorMsg(errorMsg);
        }

        return ChatJSONResult.ok();
    }

    /**
     * 查询添加好友的请求信息
     * @param userId
     * @return
     */
    @PostMapping("/queryFriendRequests")
    public ChatJSONResult queryFriendRequests(String userId) {

        log.info("request -> {}, userId -> {}", "queryFriendRequests", userId);

        if (StringUtils.isBlank(userId))
            return ChatJSONResult.errorMsg("");

        return ChatJSONResult.ok(userService.queryFriendRequestList(userId));
    }

    /**
     * 处理好友请求
     * @param acceptUserId
     * @param sendUserId
     * @param operType
     * @return
     */
    @PostMapping("/operateFriendRequests")
    public ChatJSONResult operateFriendRequests(String acceptUserId, String sendUserId, Integer operType) {

        log.info("request -> {}, acceptUserId -> {}, sendUserId -> {}, aperType -> {}", "operateFriendRequests", acceptUserId, sendUserId, operType);

        if (StringUtils.isBlank(acceptUserId) || StringUtils.isBlank(sendUserId) || operType == null ||
                StringUtils.isBlank(OperateFriendRequestsTypeEnum.getMsgByType(operType))) {

            return ChatJSONResult.errorMsg("");
        }

        if (operType == OperateFriendRequestsTypeEnum.IGNORE.type) {

            userService.delFriendRequest(acceptUserId, sendUserId);
        } else if (operType == OperateFriendRequestsTypeEnum.PASS.type) {

            userService.passFriendRequest(acceptUserId, sendUserId);
        }

        // 处理好友请求后把最新的好友列表返回到前端
        List<MyFriendsVO> myFriends = userService.queryMyFriends(acceptUserId);
        return ChatJSONResult.ok(myFriends);
    }

    /**
     * 查询我的好友列表
     * @param userId
     * @return
     */
    @PostMapping("/queryMyFriends")
    public ChatJSONResult queryMyFriends(String userId) {

        log.info("request -> {}, userId -> {}", "queryMyFriends", userId);

        if (StringUtils.isBlank(userId))
            return ChatJSONResult.errorMsg("");

        List<MyFriendsVO> myFriends = userService.queryMyFriends(userId);

        return ChatJSONResult.ok(myFriends);
    }

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptUserId
     * @return
     */
    @PostMapping("/getUnReadMsgList")
    public ChatJSONResult getUnReadMsgList(String acceptUserId) {

        log.info("request -> {}, acceptUserId -> {}", "getUnReadMsgList", acceptUserId);

        if (StringUtils.isBlank(acceptUserId)) {
            return ChatJSONResult.errorMsg("");
        }

        List<com.aiolos.pojo.ChatMsg> unreadMsgList = userService.getUnReadMsgList(acceptUserId);
        return ChatJSONResult.ok(unreadMsgList);
    }
}
