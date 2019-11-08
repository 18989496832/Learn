package com.yueyue.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 空闲检测
 *
 * @author yueyue
 * @create 2018-10-25 16:21
 */
@Slf4j
public class ServerIdleStateHandler extends IdleStateHandler {
    /**
     * 设置空闲检测时间为 30s
     */
    private static final int READER_IDLE_TIME = 30;
    public ServerIdleStateHandler() {
        /**
         * 三个的参数解释如下：
         * 1）readerIdleTime：为读超时时间（即测试端一定时间内未接受到被测试端消息）
         * 2）writerIdleTime：为写超时时间（即测试端一定时间内向被测试端发送消息）
         * 3）allIdleTime：所有类型的超时时间
         * 4) 时间类型：秒
         */
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info("{} 秒内没有读取到数据,关闭连接", READER_IDLE_TIME);
        ctx.channel().close();
    }
}
