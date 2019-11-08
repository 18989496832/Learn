package com.yueyue.netty.utils;

/**
 * @author yueyue
 * @create 2018-10-27 0:35
 */
public class MessageBaseTest {
    public static void main(String[] args) {
            /*MessageBase.Message message = MessageBase.Message.newBuilder()
                    .setRequestId(UUID.randomUUID().toString())
                    .setContent("hello world").build();
            System.out.println("message: "+message.toString());*/
        System.out.println(computeRawVarint32Size(1));
    }

    /**
     * 计算需要多少个字节
     * @param value
     * @return
     */
    public static int computeRawVarint32Size(final int value) {
        if ((value & (0xffffffff <<  7)) == 0) return 1;
        if ((value & (0xffffffff << 14)) == 0) return 2;
        if ((value & (0xffffffff << 21)) == 0) return 3;
        if ((value & (0xffffffff << 28)) == 0) return 4;
        return 5;
    }
}
