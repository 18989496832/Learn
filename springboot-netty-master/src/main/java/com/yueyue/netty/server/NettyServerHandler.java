package com.yueyue.netty.server;

import com.yueyue.netty.protocol.message.HeartbeatResponsePacket;
import com.yueyue.netty.protocol.protobuf.MessageBase;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

/**
 * @author yueyue
 * @create 2019-11-04 15:43
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<MessageBase.Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageBase.Message msg) throws Exception {
        if (msg.getCmd().equals(MessageBase.Message.CommandType.HEARTBEAT_REQUEST)) {
            log.info("收到客户端发来的心跳消息：{}", msg.toString());
            //回应pong
            //ctx.writeAndFlush(new HeartbeatResponsePacket());
            MessageBase.Message message = new MessageBase.Message()
                    .toBuilder().setCmd(MessageBase.Message.CommandType.HEARTBEAT_RESPONSE)
                    .setContent("服务端发起的客户端心跳请求应答")
                    .setRequestId(UUID.randomUUID().toString()).build();
            ctx.writeAndFlush(message);
        } else if (msg.getCmd().equals(MessageBase.Message.CommandType.NORMAL)) {
            log.info("收到客户端的业务消息：{}",msg.toString());
            /***
             * GitHub
             * 1.解包，确认业务种类
             *
             * 2.动态拼接需要处理的业务
             *
             * 3.返回业务处理结果
             *
             * 4.组包返回
             */
            String tranId = msg.getContent();
            HeartbeatResponsePacket  heartbeatResponsePacket = new HeartbeatResponsePacket();
            Map  retMap= heartbeatResponsePacket.doBiz(tranId);
            log.info("服务端准备返回的信息："+retMap);
            String retMsg = retMap.toString();
            MessageBase.Message message = new MessageBase.Message()
                    .toBuilder().setCmd(MessageBase.Message.CommandType.NORMAL)
                    .setContent(retMsg.toString())
                    .setRequestId(UUID.randomUUID().toString()).build();
            ctx.writeAndFlush(message);
        }
    }
}
