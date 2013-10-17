package com.easyinsight.datafeeds;

import com.easyinsight.logging.LogClass;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: jamesboe
 * Date: 10/16/13
 * Time: 1:08 PM
 */
public class CacheTimer extends TimerTask {

    private static CacheTimer singleton;

    private Timer timer;

    public static void initialize() {
        singleton = new CacheTimer();
    }

    public static CacheTimer instance() {
        return singleton;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(this, new Date(), 60000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void run() {
        try {
            CachedAddonDataSource.runUpdates();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
