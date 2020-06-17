package com.amo.chartserver;

import com.amo.chartserver.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NettyServer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        //new 一个主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //new 一个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer())
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 256)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            int port = 12354;

            ChannelFuture cf = bootstrap.bind(port).sync();
            log.info("服务器启动开始监听端口: {}", port);

            cf.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("服务器监听端口: {} 成功 !", port);
                } else {
                    log.error("服务器监听端口: {} 失败 !", port);
                }
            });

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
        }
    }
}

