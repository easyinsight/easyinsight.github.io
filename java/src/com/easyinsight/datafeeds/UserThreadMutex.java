package com.easyinsight.datafeeds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * User: jamesboe
 * Date: Apr 26, 2010
 * Time: 10:54:19 AM
 */
public class UserThreadMutex {

    private static final UserThreadMutex mutex = new UserThreadMutex();

    private Map<Long, Semaphore> mutexMap = new HashMap<Long, Semaphore>();

    public static UserThreadMutex mutex() {
        return mutex;
    }

    public void acquire(long userID) throws InterruptedException {
        /*Semaphore semaphore = mutexMap.get(userID);
        if (semaphore == null) {
            semaphore = new Semaphore(1);
            mutexMap.put(userID, semaphore);
            semaphore.acquire();
        } else {
            semaphore.acquire();
        }*/
    }

    public void release(long userID) {
        /*Semaphore semaphore = mutexMap.get(userID);
        if (semaphore != null) {
            semaphore.release();
        }*/
    }
}
