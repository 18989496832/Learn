package NETTY;

import NETTY.Receive;
import NETTY.Send;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServertHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Send send = (Send) msg;
		System.out.println("client发送："+send);

		Receive receive = new Receive();
		receive.setId(send.getId());
		receive.setMessage(send.getMessage());
		receive.setName(send.getName());
		ctx.writeAndFlush(receive);
	}

}