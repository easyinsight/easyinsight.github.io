package com.easyinsight.datafeeds;

import com.easyinsight.analysis.GenericReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.servlet.SystemSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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

    public boolean acquire(long userID) {

        boolean success = false;

        int tries = 0;
        while (!success && tries < 5) {
            try {
                Semaphore semaphore = mutexMap.get(userID);
                if (semaphore == null) {
                    semaphore = new Semaphore(SystemSettings.instance().getSemaphoreLimit());
                    mutexMap.put(userID, semaphore);
                    success = semaphore.tryAcquire();
                    if (!success) {
                        System.out.println(userID + " could not retrieve a user thread semaphore, retrying and waiting.");
                        success = semaphore.tryAcquire(60000, TimeUnit.MILLISECONDS);
                        if (!success) {
                            System.out.println("No luck, incrementing tries to " + (tries + 1));
                            tries++;
                        }
                    }
                    //return success;
                } else {
                    success = semaphore.tryAcquire();
                    if (!success) {
                        System.out.println(userID + " could not retrieve a user thread semaphore, retrying and waiting.");
                        success = semaphore.tryAcquire(60000, TimeUnit.MILLISECONDS);
                        if (!success) {
                            System.out.println("No luck, incrementing tries to " + (tries + 1));
                            tries++;
                        }
                    }
                    //return success;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!success) {
            throw new ReportException(new GenericReportFault("Too many requests, wait a bit for this report to run."));
        }

        return success;
    }

    public static Map<Long, Semaphore> summarize() {
        return new HashMap<Long, Semaphore>(mutex().mutexMap);
    }

    public static void release() {
        Map<Long, Semaphore> copy = new HashMap<Long, Semaphore>(mutex().mutexMap);
        for (Map.Entry<Long, Semaphore> entry : copy.entrySet()) {
            entry.getValue().release();
        }
    }

    public void release(long userID) {
        Semaphore semaphore = mutexMap.get(userID);
        if (semaphore != null) {
            semaphore.release();
        }
    }
}
