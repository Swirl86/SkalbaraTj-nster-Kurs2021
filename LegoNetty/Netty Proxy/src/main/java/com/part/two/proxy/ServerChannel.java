package com.part.two.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

public class ServerChannel extends ChannelInitializer<SocketChannel> {

    private final Channel channel;

    public ServerChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline()
                .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx,
                                                ByteBuf byteBuf) throws Exception {
                        ByteBuf copy = Unpooled.copiedBuffer(byteBuf);
                        channel.writeAndFlush(copy);

                        /* If 5 it may contain value 0, end of message */
                        /*int size = byteBuf.readableBytes();
                        if(size == 5) channel.close();*/
                    }
                });
    }
}