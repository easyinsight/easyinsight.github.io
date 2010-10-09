package com.easyinsight.datafeeds;

import com.easyinsight.eventing.MessageUtils;
import com.easyinsight.scorecard.DataSourceRefreshEvent;
import com.easyinsight.security.SecurityUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * User: jamesboe
 * Date: Apr 26, 2010
 * Time: 10:54:19 AM
 */
public class DataTypeMutex {

    private static final DataTypeMutex mutex = new DataTypeMutex();

    private Map<FeedType, Semaphore> mutexMap = new HashMap<FeedType, Semaphore>();

    private Set<FeedType> lockRequiredTypes;

    private DataTypeMutex() {
        lockRequiredTypes = new HashSet<FeedType>();
        lockRequiredTypes.add(FeedType.BASECAMP_MASTER);
        lockRequiredTypes.add(FeedType.HIGHRISE_COMPOSITE);
        for (FeedType feedType : lockRequiredTypes) {
            Semaphore semaphore = new Semaphore(3, true);
            mutexMap.put(feedType, semaphore);
        }
    }

    public static DataTypeMutex mutex() {
        return mutex;
    }

    public void lock(FeedType feedType, long dataSourceID) {
        if (lockRequiredTypes.contains(feedType)) {
            Semaphore semaphore = mutexMap.get(feedType);
            if (semaphore.tryAcquire()) {
                System.out.println("got the lock for " + feedType.getType());
            } else {
                DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                info.setDataSourceID(dataSourceID);
                info.setDataSourceName("The target system is having trouble with volume. It may take a minute or two for us to start synchronizing.");
                info.setType(DataSourceRefreshEvent.BLOCKED);
                System.out.println("user id = " + SecurityUtil.getUserID() + ", data ID " + dataSourceID);
                info.setUserId(SecurityUtil.getUserID());
                System.out.println("sending system blocked event");
                MessageUtils.sendMessage("generalNotifications", info);
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                }
            }

        }
    }

    public void unlock(FeedType feedType) {
        if (lockRequiredTypes.contains(feedType)) {
            Semaphore semaphore = mutexMap.get(feedType);            
            semaphore.release();
        }
    }
}