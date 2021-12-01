package com.part.two.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class ProxyChannel extends ChannelInitializer<SocketChannel> {

    private final Proxy proxy;

    public ProxyChannel(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        Server server = proxy.getNextServer();

        Bootstrap bootstrap = new Bootstrap();
        Channel channel = bootstrap.group(proxy.getWorkerGroup())
                .channel(NioSocketChannel.class)
                .handler(new ServerChannel(socketChannel))
                .connect(server.getAddress(), server.getPort()).sync().channel();

        socketChannel.pipeline()
                .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
                        System.out.println("Current port: " + server.getPort());
                        String string = changeConnectionTypeToClose(byteBuf);
                        ByteBuf header =  Unpooled.copiedBuffer(string, StandardCharsets.UTF_8);
                        channel.writeAndFlush(header);
                        proxy.countUpRoundRobinIndex();
                    }
                });
    }

    /* Change to Connection: close to make RoundRobin work, if the channel is not closed servers will not rotate
       after ServerChannel class have writeAndFlush msg because it will be kept alive */
    private String changeConnectionTypeToClose(ByteBuf byteBuf) {
        int count = byteBuf.readableBytes();
        byte[] buffer = new byte[count];
        byteBuf.readBytes(buffer, 0, count);
        String string = new String(buffer, 0, count);
        if(string.toLowerCase().contains("keep-alive"))
            // Case-insensitive (?i)  and  Pattern.quote() protect the search string from being interpreted as a regex
            string = string.replaceAll("(?i)"+ Pattern.quote("keep-alive"), "close");
        return string;
    }
}
