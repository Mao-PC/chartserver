package com.amo.chartserver.handler;

import com.alibaba.fastjson.JSON;
import com.amo.chartclient.proto.ChartMsgProto;
import com.amo.chartserver.dao.UserDao;
import com.amo.chartserver.util.SpringUtil;
import com.amo.chartserver.vo.User;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.data.domain.Example;

public class LoginHandler extends SimpleChannelInboundHandler<ChartMsgProto.ChartMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChartMsgProto.ChartMsg chartMsg) throws Exception {
        if (chartMsg.getType() == ChartMsgProto.ChartMsg.DataType.UserType) {

            // 转换数据类型
            JsonFormat.Printer printer = JsonFormat.printer();
            String userjson = printer.print(chartMsg.getUser());
            User user = JSON.parseObject(userjson, User.class);

            // 查询是否登录成功
            UserDao userDao = SpringUtil.getBean(UserDao.class);
            long count = userDao.count(Example.of(user));

            ChartMsgProto.ChartMsg.Builder msgBuilder = ChartMsgProto.ChartMsg.newBuilder();
            msgBuilder.setType(ChartMsgProto.ChartMsg.DataType.RespType);
            ChartMsgProto.Resp.Builder resp = ChartMsgProto.Resp.newBuilder();
            if (count == 0) {
                resp.setState(false);
                // 没有登录 验证用户名是否存在
                count = userDao.countByUsername(user.getUsername());
                if (count == 0) {
                    resp.setMsg("用户不存在");
                } else {
                    resp.setMsg("用户名或密码错误");
                }
            } else {
                resp.setState(true);
            }
            resp.build();
            msgBuilder.setResp(resp);
            ctx.channel().writeAndFlush(msgBuilder.build());
        } else {
            // 通知执行下一个InboundHandler
            ctx.fireChannelRead(chartMsg);
        }
    }
}
