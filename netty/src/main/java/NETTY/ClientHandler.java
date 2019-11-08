package NETTY;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	  @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        cause.printStackTrace();
	        ctx.close();
	    }
 
	    @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg)
	            throws Exception {
	    	Receive receive = (Receive) msg;
	        System.out.println("server反馈："+receive);
	    }
}