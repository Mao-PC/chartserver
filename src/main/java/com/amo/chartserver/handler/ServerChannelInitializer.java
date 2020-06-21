package com.amo.chartserver.handler;

import com.amo.chartclient.proto.ChartMsgProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("encoder", new ProtobufEncoder());
        pipeline.addLast("decoder", new ProtobufDecoder(ChartMsgProto.ChartMsg.getDefaultInstance()));
        pipeline.addLast(new IdleStateHandler(30, 60, 60 * 30));
        pipeline.addLast(new HeartBeatHandler());
        pipeline.addLast(new LoginHandler());
    }
}
