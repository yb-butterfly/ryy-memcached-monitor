package com.ryy.data.conf.persistent;

import java.io.Serializable;

/**
 * 连接对象实体
 * Created by xujb on 2016/1/14.
 */
public class ConnectObject implements Serializable {
    private String host;
    private int port;

    public ConnectObject(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ConnectObject{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
