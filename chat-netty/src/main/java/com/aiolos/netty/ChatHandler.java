package com.aiolos.netty;

import com.aiolos.SpringUtil;
import com.aiolos.enums.MsgActionEnum;
import com.aiolos.service.IUserService;
import com.aiolos.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aiolos
 * @date 2019-03-19 09:55
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的channel
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {

        // 获取客户端发送过来的消息
        String content = textWebSocketFrame.text();
        log.info("收到消息,channelId -> {}, content -> {}", ctx.channel().id().asShortText(), content);

        Channel currentChannel = ctx.channel();

        // 解析消息
        DataContent dataContent = JsonUtils.string2Obj(content, DataContent.class);
        Integer action = dataContent.getAction();
        // 判断消息类型，根据不同的类型来处理不同的业务
        if (action == MsgActionEnum.CONNECT.type) {
            // 当websocket第一次open的时候，初始化channel，把channel和userid关联起来
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(senderId, currentChannel);
        } else if (action == MsgActionEnum.CHAT.type) {
            // 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgText = chatMsg.getMsg();
            String senderId = chatMsg.getSenderId();
            String receiverId = chatMsg.getReceiverId();

            // 保存消息到数据库，并且消息为未签收
            IUserService userService = (IUserService) SpringUtil.getBean("userServiceImpl");
            String msgId = userService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);

            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);

            // 发送消息
            // 从全局用户Channel中获取接收方的channel
            Channel receiverChannel = UserChannelRel.get(receiverId);
            if (receiverChannel == null) {
                // TODO 推送消息
            } else {
                // 当receiverChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
                Channel findChannel = users.find(receiverChannel.id());
                if (findChannel != null) {
                    // 用户在线
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2String(dataContentMsg)));
                } else {
                    // 用户离线 TODO 推送消息
                }
            }

        } else if (action == MsgActionEnum.SIGNED.type) {
            // 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
            IUserService userService = (IUserService) SpringUtil.getBean("userServiceImpl");

            // 扩展字段在signed类型的消息中代表需要去签收的消息，逗号分隔
            String msgIdsStr = dataContent.getExtend();
            String msgIds[] = msgIdsStr.split(",");

            List<String> msgIdList = new ArrayList<>();

            for (String msgId : msgIds) {

                if (StringUtils.isNotEmpty(msgId)) {
                    msgIdList.add(msgId);
                }
            }

            if (msgIdList != null && msgIdList.size() > 0) {

                // 批量签收
                userService.updateMsgSigned(msgIdList);
            }

        } else if (action == MsgActionEnum.KEEPALIVE.type) {
            // 心跳类型的消息
            log.info("收到channel为[{}]的心跳包...", currentChannel);
        }
    }

    /**
     * 客户端连接服务端后，获取客户端的channel放到hannelGroup中去进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

//        log.info("客户端断开，channel对应的长id为：" + ctx.channel().id().asLongText());
        log.info("客户端断开，channel对应的短id为：" + ctx.channel().id().asShortText());

        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        // 发生异常后关闭channel连接，随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
