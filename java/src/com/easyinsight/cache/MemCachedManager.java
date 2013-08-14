package com.easyinsight.cache;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.logging.LogClass;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: Alan
 * Date: 5/23/13
 * Time: 10:52 AM
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

    public static void add(String key, int expiration, Object o) {
        try {
            client.add(key, expiration, o);
        } catch (Exception e) {
            //LogClass.error(e);
        }
    }

    @Nullable
    public static Object get(String key) {
        try {
            return client.get(key);
        } catch (Exception e) {
            //LogClass.error(e);
            return null;
        }
    }

    @Nullable
    public static OperationFuture<Boolean> delete(String key) {
        try {
            return client.delete(key);
        } catch (Exception e) {
            //LogClass.error(e);
            return null;
        }
    }
}
