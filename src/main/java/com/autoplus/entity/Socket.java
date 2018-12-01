package com.autoplus.entity;

public class Socket {
    private final String ip;
    private final int port;

    public Socket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}