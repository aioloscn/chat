package com.aiolos.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Aiolos
 * @date 2019-04-04 11:09
 */
public class WSServer {

    public static void main(String[] args) throws Exception {

        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitializer());

            ChannelFuture future = server.bind(8088).sync();

            future.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
