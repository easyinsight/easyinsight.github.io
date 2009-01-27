package com.easyinsight.userupload;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * User: James Boe
 * Date: Jul 23, 2008
 * Time: 7:43:28 PM
 */
public class AsyncPersistanceManager {

    private ExecutorService executorService;
    private FeedStorage feedStorage = new FeedStorage();
    private static AsyncPersistanceManager manager;

    private AsyncPersistanceManager() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public static AsyncPersistanceManager instance() {
        if (manager == null) {
            manager = new AsyncPersistanceManager();
        }
        return manager;
    }

    public void addAsyncPersistence(AsyncPersistence asyncPersistence) {
        executorService.submit(new PersistanceTask(asyncPersistence));
    }

    private class PersistanceTask implements Runnable {
        private AsyncPersistence asyncPersistence;

        private PersistanceTask(AsyncPersistence asyncPersistence) {
            this.asyncPersistence = asyncPersistence;
        }

        public void run() {
            try {
                LogClass.debug("starting async run...");
                long feedID = asyncPersistence.getFeedID();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);

                //DataRetrievalManager.instance().storeData(feedID, asyncPersistence.getDataSet());
                //feedDefinition.setSize(size);
                //feedStorage.updateDataFeedConfiguration(feedDefinition);
                LogClass.debug("async done");
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
    }
}
