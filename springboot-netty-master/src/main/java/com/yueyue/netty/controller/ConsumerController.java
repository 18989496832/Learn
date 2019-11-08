package com.yueyue.netty.controller;

import com.yueyue.netty.client.NettyClient;
import com.yueyue.netty.protocol.protobuf.MessageBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author yueyue
 * @create 2019-11-04 16:47
 */
@RestController
public class ConsumerController {
    @Autowired
    private NettyClient nettyClient;

    @GetMapping("/send")
    public String send() {
        String[] msg = {"0001"};//,"0002","0003"
        for (int i=0;i<msg.length;i++){
            MessageBase.Message message = new MessageBase.Message()
                    .toBuilder().setCmd(MessageBase.Message.CommandType.NORMAL)
                    .setContent(msg[i])
                    .setRequestId(UUID.randomUUID().toString())
                    .build();
            nettyClient.sendMsg(message);
        }
        return "send ok";
    }
    @GetMapping("/getJson")
    public String jsonSend(){
        String msg= "";
        MessageBase.Message message = new MessageBase.Message()
                .toBuilder().setCmd(MessageBase.Message.CommandType.NORMAL)
                .setContent(msg)
                .setRequestId(UUID.randomUUID().toString()).build();
        nettyClient.sendMsg(message);
        return "json String";
    }
}
