package com.aiolos.dao;

import com.aiolos.pojo.Users;
import com.aiolos.pojo.vo.FriendRequestVO;
import com.aiolos.utils.MyMapper;

import java.util.List;

public interface UsersMapperCustom extends MyMapper<Users> {

    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
}