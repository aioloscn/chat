package com.aiolos.controller;

import com.aiolos.pojo.Users;
import com.aiolos.pojo.vo.UsersVO;
import com.aiolos.service.IUserService;
import com.aiolos.utils.ChatJSONResult;
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

    @PostMapping("/registOrLogin")
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
}
