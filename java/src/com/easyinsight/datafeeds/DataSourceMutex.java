package com.easyinsight.datafeeds;

import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Apr 26, 2010
 * Time: 10:54:19 AM
 */
public class DataSourceMutex {

    private static final DataSourceMutex mutex = new DataSourceMutex();

    private Map<Long, Object> mutexMap = new HashMap<Long, Object>();

    public static DataSourceMutex mutex() {
        return mutex;
    }

    public boolean lock(long dataSourceID) {
        Object obj = mutexMap.get(dataSourceID);
        if (obj == null) {
            System.out.println(Thread.currentThread().getId() + " got the lock for " + dataSourceID);
            obj = new Object();
            mutexMap.put(dataSourceID, obj);
            return true;
        } else {
            System.out.println(Thread.currentThread().getId() + " someone else had the lock for " + dataSourceID);
            synchronized(obj) {
                try {
                    obj.wait(600000);
                } catch (InterruptedException e) {
                }
            }
            return false;
        }
    }

    public void unlock(long dataSourceID) {
        Object obj = mutexMap.remove(dataSourceID);
        if (obj != null) {
            synchronized(obj) {
                obj.notifyAll();
            }
        }
    }
}
