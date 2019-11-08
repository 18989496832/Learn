package com.yueyue.netty.server;

import com.yueyue.netty.protocol.protobuf.MessageBase;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author yueyue
 * @create 2019-11-04 15:20
 * 对channel序列化处理
 * ServerIdleStateHandler：添加心跳检查机制
 * ProtobufVarint32FrameDecoder：对protobuf协议的的消息头上加上一个长度为32的整形字段，用于标志这个消息的长度。这里是官方举的例子，实际这个字段的长度是5byte
 *
 *
 *
 *
 */
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                //空闲检测
                .addLast(new ServerIdleStateHandler())//心跳检查
                .addLast(new ProtobufVarint32FrameDecoder())//编码器
                .addLast(new ProtobufDecoder(MessageBase.Message.getDefaultInstance()))//编码
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new NettyServerHandler());
    }
}
