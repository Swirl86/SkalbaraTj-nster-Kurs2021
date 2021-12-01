package com.part.two.proxy;

public class Main {


    public static void main(String[] args) {
        Proxy proxy = new Proxy(6000);
        proxy.load();
        proxy.start();
    }
}
