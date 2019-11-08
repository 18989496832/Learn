package com.yueyue.netty.protocol.message.decoder;

import com.yueyue.netty.protocol.message.HeartbeatRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义解码器
 *
 * @author yueyue
 * @create 2019-11-04 20:19
 */
@Slf4j
public class HeartbeatDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte version = in.readByte();
        byte command = in.readByte();
        log.info("version : {}, command : {}", version, command);
        HeartbeatRequestPacket requestPacket = new HeartbeatRequestPacket();
        requestPacket.setVersion(version);
        out.add(requestPacket);
    }
}
