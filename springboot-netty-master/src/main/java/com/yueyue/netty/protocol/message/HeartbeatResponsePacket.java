package com.yueyue.netty.protocol.message;

import com.yueyue.netty.bizfactory.BizFactory;
import com.yueyue.netty.protocol.message.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yueyue
 * @create 2018-10-25 16:16
 */
public class HeartbeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }

    public Map doBiz(String tranId){
        BizFactory bizFactory = new BizFactory();
        return bizFactory.doBizModel(tranId);
    }
}
