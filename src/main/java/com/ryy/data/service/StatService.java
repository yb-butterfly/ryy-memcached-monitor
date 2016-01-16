package com.ryy.data.service;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by xujb on 2016/1/14.
 */
public class StatService {

    private MemcachedClient memcachedClient;
    private InetSocketAddress inetSocketAddress;

    /**
     * 测试一个连接是可通
     *
     * @param host
     * @param port
     * @return
     */
    public boolean testConnect(String host, int port) {
        try {
            MemcachedClient tmpClient = new MemcachedClient(new InetSocketAddress(host, port));
            tmpClient.getStats();

            int num = tmpClient.getAvailableServers().size();
            if (num == 0) {
                tmpClient.shutdown();
                return false;
            }

            tmpClient.shutdown();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean connect(String host, int port) {

        if (memcachedClient != null) {
            memcachedClient.shutdown();
            memcachedClient = null;
        }
        try {
            inetSocketAddress = new InetSocketAddress(host, port);
            memcachedClient = new MemcachedClient(inetSocketAddress);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            inetSocketAddress = null;
            memcachedClient = null;
            return false;
        }
    }

    public boolean isConnected() {
        try {
            int num = memcachedClient.getAvailableServers().size();
            return num > 0;
        } catch (Exception e) {
            // DO NOTHING
        }

        return false;
    }

    public Map<String, String> getStatMap() {
        return memcachedClient.getStats().get(inetSocketAddress);
    }

    public Map<String, String> getStatSlabsMap() {
        return memcachedClient.getStats("slabs").get(inetSocketAddress);
    }

    public void close() {
        if (memcachedClient != null) {
            memcachedClient.shutdown();
        }
    }

    public void test() {

    }

}
