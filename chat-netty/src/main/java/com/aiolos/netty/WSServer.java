package com.aiolos.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Aiolos
 * @date 2019-03-19 00:13
 */
@Component
@Slf4j
public class WSServer {

    private static class SingletonWSServer {
        static final WSServer instance = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletonWSServer.instance;
    }

    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;
    private ServerBootstrap server;
    public ChannelFuture future;

    public WSServer() {

        parentGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(new WSServerInitializer());
    }

    public void start() {

        this.future = server.bind(8088);
        log.info("netty websocket server is started");
    }
}
