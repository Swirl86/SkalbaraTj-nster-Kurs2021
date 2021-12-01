package com.part.two.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Proxy {

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final List<Server> servers;

    public Proxy(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.servers = new ArrayList<>();
    }

    public void load() {
        File file = new File("servers.txt");
        if(!file.exists())
            System.out.println("Failed to load file, does it exist?");

        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] serverInfo = line.split(" ");
                servers.add(new Server(serverInfo[0], Integer.parseInt(serverInfo[1])));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        System.out.println("Proxy port:  " + port);

        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyChannel(this))
                    .bind(this.port).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }


    int roundRobinIndex = 0;
    public Server getNextServer() {
        if(roundRobinIndex == servers.size())
            roundRobinIndex = 0;

        return servers.get(roundRobinIndex);
    }

    /* Increment index variable separately because InitChannel in ProxyChannel runs twice */
    public void countUpRoundRobinIndex() {
        roundRobinIndex++;
    }
}