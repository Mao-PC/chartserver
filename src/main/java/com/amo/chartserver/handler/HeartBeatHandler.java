package com.amo.chartserver.handler;

import com.amo.chartclient.proto.ChartMsgProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends SimpleChannelInboundHandler<ChartMsgProto.ChartMsg> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                ctx.channel().close();
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChartMsgProto.ChartMsg chartMsg) throws Exception {
        if (chartMsg.getType() == ChartMsgProto.ChartMsg.DataType.HeartBeat) {
            // 心跳
            ChartMsgProto.ChartMsg.Builder msg = ChartMsgProto.ChartMsg.newBuilder();
            msg.setType(ChartMsgProto.ChartMsg.DataType.HeartBeat);
            msg.setResp(ChartMsgProto.Resp.newBuilder());
            ctx.writeAndFlush(msg.build());
        } else {
            // 通知执行下一个InboundHandler
            ctx.fireChannelRead(chartMsg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
