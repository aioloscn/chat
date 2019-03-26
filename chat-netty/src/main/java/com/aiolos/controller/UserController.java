package com.aiolos.controller;

import com.aiolos.pojo.Users;
import com.aiolos.pojo.bo.UsersBO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

        log.info("user -> {}", JSON.toJSONString(user));

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

        log.info("usersBO -> {}", JSON.toJSONString(usersBO));

        // 获取前段传过来的base64字符串，然后转换为文件对象再上传
        String base64Data = usersBO.getFaceData();
        String userFacePath = "E:\\IdeaProjects\\" + usersBO.getUserId() + "userface64.png";
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
}
