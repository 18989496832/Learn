package NETTY4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 客户端处理器
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取应答信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = (FullHttpResponse) msg;
        ByteBuf content = response.content();
        HttpHeaders headers = response.headers();
        String respBody = content.toString(CharsetUtil.UTF_8);
        System.out.println("应答报文【"+respBody+"】");
        System.out.println("content:" + System.getProperty("line.separator") + content.toString(CharsetUtil.UTF_8));
        System.out.println("headers:" + System.getProperty("line.separator") + headers.toString());
    }

    /**
     * 发送信息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().pipeline().addLast(new ServerIdleStateHandler());
        URI url = new URI("/");
        String meg = "{H:AAAAAAAAAAAAAAA@@222222222222222222222221!!!!!!!!^^^^^&&&&&&&&&&}\\r\\n<?xml version=\"1.0\" encoding=\"UTF-8\"?><users><user id=\"0\"><name>Alexia</name><age>23</age><sex>Female</sex></user><user id=\"1\"><name>Edward</name><age>24</age><sex>Male</sex></user><user id=\"2\"><name>wjm</name><age>23</age><sex>Female</sex></user><user id=\"3\"><name>wh</name><age>24</age><sex>Male</sex></user></users>";
        System.out.println("客户端发送报文：【"+meg+"】");
        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.GET, url.toASCIIString(), Unpooled.wrappedBuffer(meg.getBytes("UTF-8")));
        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        //发送数据
        ctx.writeAndFlush(request);
    }
}