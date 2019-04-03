package com.aiolos.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Size;

/**
 * 用于检测channel的心跳handler
 * @author Aiolos
 * @date 2019-04-03 11:57
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // 判断evt是否是IdleStateEvent（用于触发用户时间，包含读空闲/写空闲/读写空闲）
        if (evt instanceof IdleStateEvent) {

            IdleStateEvent event = (IdleStateEvent)evt;

            if (event.state() == IdleState.READER_IDLE) {
                log.info("进入读空闲...");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("进入写空闲...");
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("进入读写空闲...");

                Channel channel = ctx.channel();
                // 关闭无用的channel
                channel.close();
                log.info("当前user的数量为：" + ChatHandler.users.size());
            }
        }
    }
}
