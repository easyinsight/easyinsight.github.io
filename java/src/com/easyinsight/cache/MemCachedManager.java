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

    public static void flush() {
        try {
            client.flush();
        } catch (Exception e) {
        }
    }

    public static void add(String key, int expiration, Object o) {
        try {
            client.add(key, expiration, o);
        } catch (Exception e) {
            //LogClass.error(e);
        }
    }

    public static void addReport(long reportID, Object o) {
        try {
            String key = "report" + reportID;
            client.add(key, 10000, o);
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
    public static Object getReport(long reportID) {
        try {
            String key = "report" + reportID;
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

    @Nullable
    public static OperationFuture<Boolean> deleteReport(long reportID) {
        try {
            String key = "report" + reportID;
            return client.delete(key);
        } catch (Exception e) {
            //LogClass.error(e);
            return null;
        }
    }
}
