package NETTY4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;

/**
 * HttpServer业务处理
 * 
 * @author AlanLee
 * @version 2018/01/11
 *
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception
    {
        String method = msg.method().name(); // 请求方式
        String url = msg.uri().toLowerCase(); // 请求路径
        System.out.println("请求方式"+method);
        System.out.println("请求路径"+url);

        // 接收请求内容并打印
        ByteBuf byteBuf = msg.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String requestStr = new String(bytes, "UTF-8");
        System.out.println("请求报文【"+requestStr+"】");
        /***
         * ..........................................
         * 业务可以在这里处理
         * 1.
         * 2.
         * 3.
         * 4.
         * ..........................................
         */
        String responseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<users>\n" +
                "    <user>\n" +
                "        <name>Alexia</name>\n" +
                "        <age>23</age>\n" +
                "        <sex>Female</sex>\n" +
                "        <retcode>success</retcode>\n" +
                "        <retDesc>成功</retDesc>\n" +
                "    </user>\n" +
                "</users>";
        System.out.println("应答报文【"+responseStr+"】");
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(responseStr.getBytes()));

        HttpHeaders heads = response.headers();
        // 返回内容的MIME类型
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        // 响应体的长度
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 表示是否需要持久连接
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        // 响应给客户端
        ctx.write(response);
    }

    /**
     * 数据发送完毕，则关闭连接通道.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    /**
     * 发生异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("exceptionCaught");
        if (null != cause)
            cause.printStackTrace();
        if (null != ctx)
            ctx.close();
    }

}