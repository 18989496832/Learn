package com.yueyue.netty.protocol.message;

import lombok.Data;

/**
 * 包
 *
 * @author yueyue
 * @create 2018-10-25 16:10
 */
@Data
public abstract class Packet {
    /**
     * 版本
     */
    private Byte version = 1;

    public abstract Byte getCommand();
}