package NETTY3;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码的意思，我用注释标明了，逻辑很简单。无论是FullHttpResponse，还是 ctx.writeAndFlush都是netty的api，看名知意即可。半蒙半猜之下，也可以看明白这个handler其实是：1 .获取请求uri，2. 组装返回的响应内容，3. 响应对融到客户端。需要解释的是100 Continue的问题:
 * 100 Continue含义
 * HTTP客户端程序有一个实体的主体部分要发送给服务器，但希望在发送之前查看下服务器是否会接受这个实体，所以在发送实体之前先发送了一个携带100 Continue的Expect请求首部的请求。
 * 服务器在收到这样的请求后，应该用 100 Continue或一条错误码来进行响应。
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        //100 Continue
        if (HttpUtil.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(
                           HttpVersion.HTTP_1_1,
                           HttpResponseStatus.CONTINUE));
        }
        // 获取请求的uri
        String uri = req.uri();
        Map<String,String> resMap = new HashMap<>();
        resMap.put("method",req.method().name());
        resMap.put("uri",uri);
        String msg = "<html><head><title>test</title></head><body>你请求uri为：" + uri+"</body></html>";
       // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                                        HttpVersion.HTTP_1_1,
                                        HttpResponseStatus.OK,
                                        Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
       // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
       // 将html write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}