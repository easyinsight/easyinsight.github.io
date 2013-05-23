package com.easyinsight.cache;

import com.easyinsight.config.ConfigLoader;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 5/23/13
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class MemCachedManager {

    private static MemcachedClient client;

    public static void initialize() {
        try {
            client = new MemcachedClient(AddrUtil.getAddresses(ConfigLoader.instance().getMemcachedUrl()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MemcachedClient instance() {
        return client;
    }
}
