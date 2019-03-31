package com.aiolos.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * 用户id和channel关联
 * @author Aiolos
 * @date 2019-03-31 22:22
 */
public class UserChannelRel {

    private static HashMap<String, Channel> manager = new HashMap<>();

    public static void put(String senderId, Channel channel) {

        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {

        return manager.get(senderId);
    }
}
